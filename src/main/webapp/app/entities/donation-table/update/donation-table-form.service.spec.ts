import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../donation-table.test-samples';

import { DonationTableFormService } from './donation-table-form.service';

describe('DonationTable Form Service', () => {
  let service: DonationTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DonationTableFormService);
  });

  describe('Service methods', () => {
    describe('createDonationTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDonationTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            donationId: expect.any(Object),
            donationType: expect.any(Object),
            donationDesc: expect.any(Object),
            donationMonth: expect.any(Object),
            donationCost: expect.any(Object),
            roomId: expect.any(Object),
            status: expect.any(Object),
            roomTable: expect.any(Object),
          }),
        );
      });

      it('passing IDonationTable should create a new form with FormGroup', () => {
        const formGroup = service.createDonationTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            donationId: expect.any(Object),
            donationType: expect.any(Object),
            donationDesc: expect.any(Object),
            donationMonth: expect.any(Object),
            donationCost: expect.any(Object),
            roomId: expect.any(Object),
            status: expect.any(Object),
            roomTable: expect.any(Object),
          }),
        );
      });
    });

    describe('getDonationTable', () => {
      it('should return NewDonationTable for default DonationTable initial value', () => {
        const formGroup = service.createDonationTableFormGroup(sampleWithNewData);

        const donationTable = service.getDonationTable(formGroup) as any;

        expect(donationTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewDonationTable for empty DonationTable initial value', () => {
        const formGroup = service.createDonationTableFormGroup();

        const donationTable = service.getDonationTable(formGroup) as any;

        expect(donationTable).toMatchObject({});
      });

      it('should return IDonationTable', () => {
        const formGroup = service.createDonationTableFormGroup(sampleWithRequiredData);

        const donationTable = service.getDonationTable(formGroup) as any;

        expect(donationTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDonationTable should not enable id FormControl', () => {
        const formGroup = service.createDonationTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDonationTable should disable id FormControl', () => {
        const formGroup = service.createDonationTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
