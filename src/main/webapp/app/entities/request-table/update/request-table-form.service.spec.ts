import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../request-table.test-samples';

import { RequestTableFormService } from './request-table-form.service';

describe('RequestTable Form Service', () => {
  let service: RequestTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequestTableFormService);
  });

  describe('Service methods', () => {
    describe('createRequestTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRequestTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            status: expect.any(Object),
            userId: expect.any(Object),
            title: expect.any(Object),
            message: expect.any(Object),
            reply: expect.any(Object),
            note: expect.any(Object),
            notificationTable: expect.any(Object),
          }),
        );
      });

      it('passing IRequestTable should create a new form with FormGroup', () => {
        const formGroup = service.createRequestTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            status: expect.any(Object),
            userId: expect.any(Object),
            title: expect.any(Object),
            message: expect.any(Object),
            reply: expect.any(Object),
            note: expect.any(Object),
            notificationTable: expect.any(Object),
          }),
        );
      });
    });

    describe('getRequestTable', () => {
      it('should return NewRequestTable for default RequestTable initial value', () => {
        const formGroup = service.createRequestTableFormGroup(sampleWithNewData);

        const requestTable = service.getRequestTable(formGroup) as any;

        expect(requestTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewRequestTable for empty RequestTable initial value', () => {
        const formGroup = service.createRequestTableFormGroup();

        const requestTable = service.getRequestTable(formGroup) as any;

        expect(requestTable).toMatchObject({});
      });

      it('should return IRequestTable', () => {
        const formGroup = service.createRequestTableFormGroup(sampleWithRequiredData);

        const requestTable = service.getRequestTable(formGroup) as any;

        expect(requestTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRequestTable should not enable id FormControl', () => {
        const formGroup = service.createRequestTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRequestTable should disable id FormControl', () => {
        const formGroup = service.createRequestTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
