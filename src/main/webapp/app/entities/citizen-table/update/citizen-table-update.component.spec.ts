import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { CitizenTableService } from '../service/citizen-table.service';
import { ICitizenTable } from '../citizen-table.model';
import { CitizenTableFormService } from './citizen-table-form.service';

import { CitizenTableUpdateComponent } from './citizen-table-update.component';

describe('CitizenTable Management Update Component', () => {
  let comp: CitizenTableUpdateComponent;
  let fixture: ComponentFixture<CitizenTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let citizenTableFormService: CitizenTableFormService;
  let citizenTableService: CitizenTableService;
  let roomTableService: RoomTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CitizenTableUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CitizenTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CitizenTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    citizenTableFormService = TestBed.inject(CitizenTableFormService);
    citizenTableService = TestBed.inject(CitizenTableService);
    roomTableService = TestBed.inject(RoomTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call familyId query and add missing value', () => {
      const citizenTable: ICitizenTable = { id: 456 };
      const familyId: IRoomTable = { id: 19344 };
      citizenTable.familyId = familyId;

      const familyIdCollection: IRoomTable[] = [{ id: 12039 }];
      jest.spyOn(roomTableService, 'query').mockReturnValue(of(new HttpResponse({ body: familyIdCollection })));
      const expectedCollection: IRoomTable[] = [familyId, ...familyIdCollection];
      jest.spyOn(roomTableService, 'addRoomTableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ citizenTable });
      comp.ngOnInit();

      expect(roomTableService.query).toHaveBeenCalled();
      expect(roomTableService.addRoomTableToCollectionIfMissing).toHaveBeenCalledWith(familyIdCollection, familyId);
      expect(comp.familyIdsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const citizenTable: ICitizenTable = { id: 456 };
      const familyId: IRoomTable = { id: 6563 };
      citizenTable.familyId = familyId;

      activatedRoute.data = of({ citizenTable });
      comp.ngOnInit();

      expect(comp.familyIdsCollection).toContain(familyId);
      expect(comp.citizenTable).toEqual(citizenTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICitizenTable>>();
      const citizenTable = { id: 123 };
      jest.spyOn(citizenTableFormService, 'getCitizenTable').mockReturnValue(citizenTable);
      jest.spyOn(citizenTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ citizenTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: citizenTable }));
      saveSubject.complete();

      // THEN
      expect(citizenTableFormService.getCitizenTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(citizenTableService.update).toHaveBeenCalledWith(expect.objectContaining(citizenTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICitizenTable>>();
      const citizenTable = { id: 123 };
      jest.spyOn(citizenTableFormService, 'getCitizenTable').mockReturnValue({ id: null });
      jest.spyOn(citizenTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ citizenTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: citizenTable }));
      saveSubject.complete();

      // THEN
      expect(citizenTableFormService.getCitizenTable).toHaveBeenCalled();
      expect(citizenTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICitizenTable>>();
      const citizenTable = { id: 123 };
      jest.spyOn(citizenTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ citizenTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(citizenTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRoomTable', () => {
      it('Should forward to roomTableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roomTableService, 'compareRoomTable');
        comp.compareRoomTable(entity, entity2);
        expect(roomTableService.compareRoomTable).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
