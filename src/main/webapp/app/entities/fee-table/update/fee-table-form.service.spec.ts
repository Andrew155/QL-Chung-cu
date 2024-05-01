import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fee-table.test-samples';

import { FeeTableFormService } from './fee-table-form.service';

describe('FeeTable Form Service', () => {
  let service: FeeTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeeTableFormService);
  });

  describe('Service methods', () => {
    describe('createFeeTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeeTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            feeType: expect.any(Object),
            feeDesc: expect.any(Object),
            feeMonth: expect.any(Object),
            feeCost: expect.any(Object),
            date: expect.any(Object),
            status: expect.any(Object),
            feeId: expect.any(Object),
            roomTable: expect.any(Object),
          }),
        );
      });

      it('passing IFeeTable should create a new form with FormGroup', () => {
        const formGroup = service.createFeeTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            feeType: expect.any(Object),
            feeDesc: expect.any(Object),
            feeMonth: expect.any(Object),
            feeCost: expect.any(Object),
            date: expect.any(Object),
            status: expect.any(Object),
            feeId: expect.any(Object),
            roomTable: expect.any(Object),
          }),
        );
      });
    });

    describe('getFeeTable', () => {
      it('should return NewFeeTable for default FeeTable initial value', () => {
        const formGroup = service.createFeeTableFormGroup(sampleWithNewData);

        const feeTable = service.getFeeTable(formGroup) as any;

        expect(feeTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeeTable for empty FeeTable initial value', () => {
        const formGroup = service.createFeeTableFormGroup();

        const feeTable = service.getFeeTable(formGroup) as any;

        expect(feeTable).toMatchObject({});
      });

      it('should return IFeeTable', () => {
        const formGroup = service.createFeeTableFormGroup(sampleWithRequiredData);

        const feeTable = service.getFeeTable(formGroup) as any;

        expect(feeTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeeTable should not enable id FormControl', () => {
        const formGroup = service.createFeeTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeeTable should disable id FormControl', () => {
        const formGroup = service.createFeeTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
