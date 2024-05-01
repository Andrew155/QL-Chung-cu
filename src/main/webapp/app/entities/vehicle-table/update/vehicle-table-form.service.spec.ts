import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vehicle-table.test-samples';

import { VehicleTableFormService } from './vehicle-table-form.service';

describe('VehicleTable Form Service', () => {
  let service: VehicleTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleTableFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            vehicleName: expect.any(Object),
            vehicleType: expect.any(Object),
            vehicleId: expect.any(Object),
            roomId: expect.any(Object),
            ownerId: expect.any(Object),
            vehicleFee: expect.any(Object),
            roomTable: expect.any(Object),
          }),
        );
      });

      it('passing IVehicleTable should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            vehicleName: expect.any(Object),
            vehicleType: expect.any(Object),
            vehicleId: expect.any(Object),
            roomId: expect.any(Object),
            ownerId: expect.any(Object),
            vehicleFee: expect.any(Object),
            roomTable: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicleTable', () => {
      it('should return NewVehicleTable for default VehicleTable initial value', () => {
        const formGroup = service.createVehicleTableFormGroup(sampleWithNewData);

        const vehicleTable = service.getVehicleTable(formGroup) as any;

        expect(vehicleTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicleTable for empty VehicleTable initial value', () => {
        const formGroup = service.createVehicleTableFormGroup();

        const vehicleTable = service.getVehicleTable(formGroup) as any;

        expect(vehicleTable).toMatchObject({});
      });

      it('should return IVehicleTable', () => {
        const formGroup = service.createVehicleTableFormGroup(sampleWithRequiredData);

        const vehicleTable = service.getVehicleTable(formGroup) as any;

        expect(vehicleTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicleTable should not enable id FormControl', () => {
        const formGroup = service.createVehicleTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicleTable should disable id FormControl', () => {
        const formGroup = service.createVehicleTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
