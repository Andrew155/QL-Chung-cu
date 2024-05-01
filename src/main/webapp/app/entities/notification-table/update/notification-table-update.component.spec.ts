import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { NotificationTableService } from '../service/notification-table.service';
import { INotificationTable } from '../notification-table.model';
import { NotificationTableFormService } from './notification-table-form.service';

import { NotificationTableUpdateComponent } from './notification-table-update.component';

describe('NotificationTable Management Update Component', () => {
  let comp: NotificationTableUpdateComponent;
  let fixture: ComponentFixture<NotificationTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificationTableFormService: NotificationTableFormService;
  let notificationTableService: NotificationTableService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), NotificationTableUpdateComponent],
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
      .overrideTemplate(NotificationTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificationTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationTableFormService = TestBed.inject(NotificationTableFormService);
    notificationTableService = TestBed.inject(NotificationTableService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const notificationTable: INotificationTable = { id: 456 };
      const applicationTable: IApplicationUser = { id: 14435 };
      notificationTable.applicationTable = applicationTable;

      const applicationUserCollection: IApplicationUser[] = [{ id: 2207 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [applicationTable];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notificationTable });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const notificationTable: INotificationTable = { id: 456 };
      const applicationTable: IApplicationUser = { id: 2278 };
      notificationTable.applicationTable = applicationTable;

      activatedRoute.data = of({ notificationTable });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(applicationTable);
      expect(comp.notificationTable).toEqual(notificationTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationTable>>();
      const notificationTable = { id: 123 };
      jest.spyOn(notificationTableFormService, 'getNotificationTable').mockReturnValue(notificationTable);
      jest.spyOn(notificationTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationTable }));
      saveSubject.complete();

      // THEN
      expect(notificationTableFormService.getNotificationTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationTableService.update).toHaveBeenCalledWith(expect.objectContaining(notificationTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationTable>>();
      const notificationTable = { id: 123 };
      jest.spyOn(notificationTableFormService, 'getNotificationTable').mockReturnValue({ id: null });
      jest.spyOn(notificationTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificationTable }));
      saveSubject.complete();

      // THEN
      expect(notificationTableFormService.getNotificationTable).toHaveBeenCalled();
      expect(notificationTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificationTable>>();
      const notificationTable = { id: 123 };
      jest.spyOn(notificationTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificationTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
