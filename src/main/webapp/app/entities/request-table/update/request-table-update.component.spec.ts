import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { INotificationTable } from 'app/entities/notification-table/notification-table.model';
import { NotificationTableService } from 'app/entities/notification-table/service/notification-table.service';
import { RequestTableService } from '../service/request-table.service';
import { IRequestTable } from '../request-table.model';
import { RequestTableFormService } from './request-table-form.service';

import { RequestTableUpdateComponent } from './request-table-update.component';

describe('RequestTable Management Update Component', () => {
  let comp: RequestTableUpdateComponent;
  let fixture: ComponentFixture<RequestTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let requestTableFormService: RequestTableFormService;
  let requestTableService: RequestTableService;
  let notificationTableService: NotificationTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RequestTableUpdateComponent],
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
      .overrideTemplate(RequestTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    requestTableFormService = TestBed.inject(RequestTableFormService);
    requestTableService = TestBed.inject(RequestTableService);
    notificationTableService = TestBed.inject(NotificationTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call notificationTable query and add missing value', () => {
      const requestTable: IRequestTable = { id: 456 };
      const notificationTable: INotificationTable = { id: 1496 };
      requestTable.notificationTable = notificationTable;

      const notificationTableCollection: INotificationTable[] = [{ id: 24891 }];
      jest.spyOn(notificationTableService, 'query').mockReturnValue(of(new HttpResponse({ body: notificationTableCollection })));
      const expectedCollection: INotificationTable[] = [notificationTable, ...notificationTableCollection];
      jest.spyOn(notificationTableService, 'addNotificationTableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ requestTable });
      comp.ngOnInit();

      expect(notificationTableService.query).toHaveBeenCalled();
      expect(notificationTableService.addNotificationTableToCollectionIfMissing).toHaveBeenCalledWith(
        notificationTableCollection,
        notificationTable,
      );
      expect(comp.notificationTablesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const requestTable: IRequestTable = { id: 456 };
      const notificationTable: INotificationTable = { id: 28719 };
      requestTable.notificationTable = notificationTable;

      activatedRoute.data = of({ requestTable });
      comp.ngOnInit();

      expect(comp.notificationTablesCollection).toContain(notificationTable);
      expect(comp.requestTable).toEqual(requestTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequestTable>>();
      const requestTable = { id: 123 };
      jest.spyOn(requestTableFormService, 'getRequestTable').mockReturnValue(requestTable);
      jest.spyOn(requestTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: requestTable }));
      saveSubject.complete();

      // THEN
      expect(requestTableFormService.getRequestTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(requestTableService.update).toHaveBeenCalledWith(expect.objectContaining(requestTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequestTable>>();
      const requestTable = { id: 123 };
      jest.spyOn(requestTableFormService, 'getRequestTable').mockReturnValue({ id: null });
      jest.spyOn(requestTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: requestTable }));
      saveSubject.complete();

      // THEN
      expect(requestTableFormService.getRequestTable).toHaveBeenCalled();
      expect(requestTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequestTable>>();
      const requestTable = { id: 123 };
      jest.spyOn(requestTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(requestTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareNotificationTable', () => {
      it('Should forward to notificationTableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(notificationTableService, 'compareNotificationTable');
        comp.compareNotificationTable(entity, entity2);
        expect(notificationTableService.compareNotificationTable).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
