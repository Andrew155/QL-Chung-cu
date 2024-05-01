import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../notification-table.test-samples';

import { NotificationTableFormService } from './notification-table-form.service';

describe('NotificationTable Form Service', () => {
  let service: NotificationTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationTableFormService);
  });

  describe('Service methods', () => {
    describe('createNotificationTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificationTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            userID: expect.any(Object),
            applicationTable: expect.any(Object),
          }),
        );
      });

      it('passing INotificationTable should create a new form with FormGroup', () => {
        const formGroup = service.createNotificationTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            userID: expect.any(Object),
            applicationTable: expect.any(Object),
          }),
        );
      });
    });

    describe('getNotificationTable', () => {
      it('should return NewNotificationTable for default NotificationTable initial value', () => {
        const formGroup = service.createNotificationTableFormGroup(sampleWithNewData);

        const notificationTable = service.getNotificationTable(formGroup) as any;

        expect(notificationTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificationTable for empty NotificationTable initial value', () => {
        const formGroup = service.createNotificationTableFormGroup();

        const notificationTable = service.getNotificationTable(formGroup) as any;

        expect(notificationTable).toMatchObject({});
      });

      it('should return INotificationTable', () => {
        const formGroup = service.createNotificationTableFormGroup(sampleWithRequiredData);

        const notificationTable = service.getNotificationTable(formGroup) as any;

        expect(notificationTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificationTable should not enable id FormControl', () => {
        const formGroup = service.createNotificationTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificationTable should disable id FormControl', () => {
        const formGroup = service.createNotificationTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
