import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bill-table.test-samples';

import { BillTableFormService } from './bill-table-form.service';

describe('BillTable Form Service', () => {
  let service: BillTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BillTableFormService);
  });

  describe('Service methods', () => {
    describe('createBillTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBillTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            billType: expect.any(Object),
            billId: expect.any(Object),
            billMonth: expect.any(Object),
            billAmount: expect.any(Object),
            roomId: expect.any(Object),
            date: expect.any(Object),
            status: expect.any(Object),
            billCost: expect.any(Object),
            customerID: expect.any(Object),
            roomTable: expect.any(Object),
            applicationTable: expect.any(Object),
          }),
        );
      });

      it('passing IBillTable should create a new form with FormGroup', () => {
        const formGroup = service.createBillTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            billType: expect.any(Object),
            billId: expect.any(Object),
            billMonth: expect.any(Object),
            billAmount: expect.any(Object),
            roomId: expect.any(Object),
            date: expect.any(Object),
            status: expect.any(Object),
            billCost: expect.any(Object),
            customerID: expect.any(Object),
            roomTable: expect.any(Object),
            applicationTable: expect.any(Object),
          }),
        );
      });
    });

    describe('getBillTable', () => {
      it('should return NewBillTable for default BillTable initial value', () => {
        const formGroup = service.createBillTableFormGroup(sampleWithNewData);

        const billTable = service.getBillTable(formGroup) as any;

        expect(billTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewBillTable for empty BillTable initial value', () => {
        const formGroup = service.createBillTableFormGroup();

        const billTable = service.getBillTable(formGroup) as any;

        expect(billTable).toMatchObject({});
      });

      it('should return IBillTable', () => {
        const formGroup = service.createBillTableFormGroup(sampleWithRequiredData);

        const billTable = service.getBillTable(formGroup) as any;

        expect(billTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBillTable should not enable id FormControl', () => {
        const formGroup = service.createBillTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBillTable should disable id FormControl', () => {
        const formGroup = service.createBillTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
