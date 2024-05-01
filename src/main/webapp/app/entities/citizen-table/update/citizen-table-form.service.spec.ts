import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../citizen-table.test-samples';

import { CitizenTableFormService } from './citizen-table-form.service';

describe('CitizenTable Form Service', () => {
  let service: CitizenTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CitizenTableFormService);
  });

  describe('Service methods', () => {
    describe('createCitizenTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCitizenTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            citizenID: expect.any(Object),
            name: expect.any(Object),
            dob: expect.any(Object),
            contact: expect.any(Object),
            gender: expect.any(Object),
            relation: expect.any(Object),
            status: expect.any(Object),
            familyId: expect.any(Object),
          }),
        );
      });

      it('passing ICitizenTable should create a new form with FormGroup', () => {
        const formGroup = service.createCitizenTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            citizenID: expect.any(Object),
            name: expect.any(Object),
            dob: expect.any(Object),
            contact: expect.any(Object),
            gender: expect.any(Object),
            relation: expect.any(Object),
            status: expect.any(Object),
            familyId: expect.any(Object),
          }),
        );
      });
    });

    describe('getCitizenTable', () => {
      it('should return NewCitizenTable for default CitizenTable initial value', () => {
        const formGroup = service.createCitizenTableFormGroup(sampleWithNewData);

        const citizenTable = service.getCitizenTable(formGroup) as any;

        expect(citizenTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewCitizenTable for empty CitizenTable initial value', () => {
        const formGroup = service.createCitizenTableFormGroup();

        const citizenTable = service.getCitizenTable(formGroup) as any;

        expect(citizenTable).toMatchObject({});
      });

      it('should return ICitizenTable', () => {
        const formGroup = service.createCitizenTableFormGroup(sampleWithRequiredData);

        const citizenTable = service.getCitizenTable(formGroup) as any;

        expect(citizenTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICitizenTable should not enable id FormControl', () => {
        const formGroup = service.createCitizenTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCitizenTable should disable id FormControl', () => {
        const formGroup = service.createCitizenTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
