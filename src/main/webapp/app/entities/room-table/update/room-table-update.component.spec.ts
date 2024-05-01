import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomTableService } from '../service/room-table.service';
import { IRoomTable } from '../room-table.model';
import { RoomTableFormService } from './room-table-form.service';

import { RoomTableUpdateComponent } from './room-table-update.component';

describe('RoomTable Management Update Component', () => {
  let comp: RoomTableUpdateComponent;
  let fixture: ComponentFixture<RoomTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomTableFormService: RoomTableFormService;
  let roomTableService: RoomTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RoomTableUpdateComponent],
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
      .overrideTemplate(RoomTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomTableFormService = TestBed.inject(RoomTableFormService);
    roomTableService = TestBed.inject(RoomTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const roomTable: IRoomTable = { id: 456 };

      activatedRoute.data = of({ roomTable });
      comp.ngOnInit();

      expect(comp.roomTable).toEqual(roomTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomTable>>();
      const roomTable = { id: 123 };
      jest.spyOn(roomTableFormService, 'getRoomTable').mockReturnValue(roomTable);
      jest.spyOn(roomTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomTable }));
      saveSubject.complete();

      // THEN
      expect(roomTableFormService.getRoomTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomTableService.update).toHaveBeenCalledWith(expect.objectContaining(roomTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomTable>>();
      const roomTable = { id: 123 };
      jest.spyOn(roomTableFormService, 'getRoomTable').mockReturnValue({ id: null });
      jest.spyOn(roomTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomTable }));
      saveSubject.complete();

      // THEN
      expect(roomTableFormService.getRoomTable).toHaveBeenCalled();
      expect(roomTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomTable>>();
      const roomTable = { id: 123 };
      jest.spyOn(roomTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
