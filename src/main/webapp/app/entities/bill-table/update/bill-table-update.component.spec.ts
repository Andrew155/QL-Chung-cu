import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IBillTable } from '../bill-table.model';
import { BillTableService } from '../service/bill-table.service';
import { BillTableFormService } from './bill-table-form.service';

import { BillTableUpdateComponent } from './bill-table-update.component';

describe('BillTable Management Update Component', () => {
  let comp: BillTableUpdateComponent;
  let fixture: ComponentFixture<BillTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let billTableFormService: BillTableFormService;
  let billTableService: BillTableService;
  let roomTableService: RoomTableService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BillTableUpdateComponent],
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
      .overrideTemplate(BillTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BillTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    billTableFormService = TestBed.inject(BillTableFormService);
    billTableService = TestBed.inject(BillTableService);
    roomTableService = TestBed.inject(RoomTableService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RoomTable query and add missing value', () => {
      const billTable: IBillTable = { id: 456 };
      const roomTable: IRoomTable = { id: 29008 };
      billTable.roomTable = roomTable;

      const roomTableCollection: IRoomTable[] = [{ id: 18484 }];
      jest.spyOn(roomTableService, 'query').mockReturnValue(of(new HttpResponse({ body: roomTableCollection })));
      const additionalRoomTables = [roomTable];
      const expectedCollection: IRoomTable[] = [...additionalRoomTables, ...roomTableCollection];
      jest.spyOn(roomTableService, 'addRoomTableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ billTable });
      comp.ngOnInit();

      expect(roomTableService.query).toHaveBeenCalled();
      expect(roomTableService.addRoomTableToCollectionIfMissing).toHaveBeenCalledWith(
        roomTableCollection,
        ...additionalRoomTables.map(expect.objectContaining),
      );
      expect(comp.roomTablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const billTable: IBillTable = { id: 456 };
      const applicationTable: IApplicationUser = { id: 28062 };
      billTable.applicationTable = applicationTable;

      const applicationUserCollection: IApplicationUser[] = [{ id: 21047 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [applicationTable];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ billTable });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const billTable: IBillTable = { id: 456 };
      const roomTable: IRoomTable = { id: 10763 };
      billTable.roomTable = roomTable;
      const applicationTable: IApplicationUser = { id: 17565 };
      billTable.applicationTable = applicationTable;

      activatedRoute.data = of({ billTable });
      comp.ngOnInit();

      expect(comp.roomTablesSharedCollection).toContain(roomTable);
      expect(comp.applicationUsersSharedCollection).toContain(applicationTable);
      expect(comp.billTable).toEqual(billTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBillTable>>();
      const billTable = { id: 123 };
      jest.spyOn(billTableFormService, 'getBillTable').mockReturnValue(billTable);
      jest.spyOn(billTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ billTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: billTable }));
      saveSubject.complete();

      // THEN
      expect(billTableFormService.getBillTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(billTableService.update).toHaveBeenCalledWith(expect.objectContaining(billTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBillTable>>();
      const billTable = { id: 123 };
      jest.spyOn(billTableFormService, 'getBillTable').mockReturnValue({ id: null });
      jest.spyOn(billTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ billTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: billTable }));
      saveSubject.complete();

      // THEN
      expect(billTableFormService.getBillTable).toHaveBeenCalled();
      expect(billTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBillTable>>();
      const billTable = { id: 123 };
      jest.spyOn(billTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ billTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(billTableService.update).toHaveBeenCalled();
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

    describe('compareApplicationUser', () => {
      it('Should forward to applicationUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
