import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { VehicleTableService } from '../service/vehicle-table.service';
import { IVehicleTable } from '../vehicle-table.model';
import { VehicleTableFormService } from './vehicle-table-form.service';

import { VehicleTableUpdateComponent } from './vehicle-table-update.component';

describe('VehicleTable Management Update Component', () => {
  let comp: VehicleTableUpdateComponent;
  let fixture: ComponentFixture<VehicleTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleTableFormService: VehicleTableFormService;
  let vehicleTableService: VehicleTableService;
  let roomTableService: RoomTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), VehicleTableUpdateComponent],
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
      .overrideTemplate(VehicleTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleTableFormService = TestBed.inject(VehicleTableFormService);
    vehicleTableService = TestBed.inject(VehicleTableService);
    roomTableService = TestBed.inject(RoomTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RoomTable query and add missing value', () => {
      const vehicleTable: IVehicleTable = { id: 456 };
      const roomTable: IRoomTable = { id: 31649 };
      vehicleTable.roomTable = roomTable;

      const roomTableCollection: IRoomTable[] = [{ id: 25944 }];
      jest.spyOn(roomTableService, 'query').mockReturnValue(of(new HttpResponse({ body: roomTableCollection })));
      const additionalRoomTables = [roomTable];
      const expectedCollection: IRoomTable[] = [...additionalRoomTables, ...roomTableCollection];
      jest.spyOn(roomTableService, 'addRoomTableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicleTable });
      comp.ngOnInit();

      expect(roomTableService.query).toHaveBeenCalled();
      expect(roomTableService.addRoomTableToCollectionIfMissing).toHaveBeenCalledWith(
        roomTableCollection,
        ...additionalRoomTables.map(expect.objectContaining),
      );
      expect(comp.roomTablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicleTable: IVehicleTable = { id: 456 };
      const roomTable: IRoomTable = { id: 7579 };
      vehicleTable.roomTable = roomTable;

      activatedRoute.data = of({ vehicleTable });
      comp.ngOnInit();

      expect(comp.roomTablesSharedCollection).toContain(roomTable);
      expect(comp.vehicleTable).toEqual(vehicleTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleTable>>();
      const vehicleTable = { id: 123 };
      jest.spyOn(vehicleTableFormService, 'getVehicleTable').mockReturnValue(vehicleTable);
      jest.spyOn(vehicleTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleTable }));
      saveSubject.complete();

      // THEN
      expect(vehicleTableFormService.getVehicleTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleTableService.update).toHaveBeenCalledWith(expect.objectContaining(vehicleTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleTable>>();
      const vehicleTable = { id: 123 };
      jest.spyOn(vehicleTableFormService, 'getVehicleTable').mockReturnValue({ id: null });
      jest.spyOn(vehicleTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleTable }));
      saveSubject.complete();

      // THEN
      expect(vehicleTableFormService.getVehicleTable).toHaveBeenCalled();
      expect(vehicleTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleTable>>();
      const vehicleTable = { id: 123 };
      jest.spyOn(vehicleTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleTableService.update).toHaveBeenCalled();
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
