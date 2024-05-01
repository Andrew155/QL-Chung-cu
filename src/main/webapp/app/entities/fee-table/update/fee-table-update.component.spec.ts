import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { FeeTableService } from '../service/fee-table.service';
import { IFeeTable } from '../fee-table.model';
import { FeeTableFormService } from './fee-table-form.service';

import { FeeTableUpdateComponent } from './fee-table-update.component';

describe('FeeTable Management Update Component', () => {
  let comp: FeeTableUpdateComponent;
  let fixture: ComponentFixture<FeeTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feeTableFormService: FeeTableFormService;
  let feeTableService: FeeTableService;
  let roomTableService: RoomTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FeeTableUpdateComponent],
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
      .overrideTemplate(FeeTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeeTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feeTableFormService = TestBed.inject(FeeTableFormService);
    feeTableService = TestBed.inject(FeeTableService);
    roomTableService = TestBed.inject(RoomTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RoomTable query and add missing value', () => {
      const feeTable: IFeeTable = { id: 456 };
      const roomTable: IRoomTable = { id: 11291 };
      feeTable.roomTable = roomTable;

      const roomTableCollection: IRoomTable[] = [{ id: 16598 }];
      jest.spyOn(roomTableService, 'query').mockReturnValue(of(new HttpResponse({ body: roomTableCollection })));
      const additionalRoomTables = [roomTable];
      const expectedCollection: IRoomTable[] = [...additionalRoomTables, ...roomTableCollection];
      jest.spyOn(roomTableService, 'addRoomTableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feeTable });
      comp.ngOnInit();

      expect(roomTableService.query).toHaveBeenCalled();
      expect(roomTableService.addRoomTableToCollectionIfMissing).toHaveBeenCalledWith(
        roomTableCollection,
        ...additionalRoomTables.map(expect.objectContaining),
      );
      expect(comp.roomTablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feeTable: IFeeTable = { id: 456 };
      const roomTable: IRoomTable = { id: 1553 };
      feeTable.roomTable = roomTable;

      activatedRoute.data = of({ feeTable });
      comp.ngOnInit();

      expect(comp.roomTablesSharedCollection).toContain(roomTable);
      expect(comp.feeTable).toEqual(feeTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeeTable>>();
      const feeTable = { id: 123 };
      jest.spyOn(feeTableFormService, 'getFeeTable').mockReturnValue(feeTable);
      jest.spyOn(feeTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feeTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feeTable }));
      saveSubject.complete();

      // THEN
      expect(feeTableFormService.getFeeTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feeTableService.update).toHaveBeenCalledWith(expect.objectContaining(feeTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeeTable>>();
      const feeTable = { id: 123 };
      jest.spyOn(feeTableFormService, 'getFeeTable').mockReturnValue({ id: null });
      jest.spyOn(feeTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feeTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feeTable }));
      saveSubject.complete();

      // THEN
      expect(feeTableFormService.getFeeTable).toHaveBeenCalled();
      expect(feeTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeeTable>>();
      const feeTable = { id: 123 };
      jest.spyOn(feeTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feeTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feeTableService.update).toHaveBeenCalled();
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
