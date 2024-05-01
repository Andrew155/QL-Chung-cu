import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../room-table.test-samples';

import { RoomTableFormService } from './room-table-form.service';

describe('RoomTable Form Service', () => {
  let service: RoomTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoomTableFormService);
  });

  describe('Service methods', () => {
    describe('createRoomTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoomTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            roomId: expect.any(Object),
            area: expect.any(Object),
            ownTime: expect.any(Object),
            ownerId: expect.any(Object),
            ownerName: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IRoomTable should create a new form with FormGroup', () => {
        const formGroup = service.createRoomTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createAt: expect.any(Object),
            updateAt: expect.any(Object),
            deletedAt: expect.any(Object),
            roomId: expect.any(Object),
            area: expect.any(Object),
            ownTime: expect.any(Object),
            ownerId: expect.any(Object),
            ownerName: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getRoomTable', () => {
      it('should return NewRoomTable for default RoomTable initial value', () => {
        const formGroup = service.createRoomTableFormGroup(sampleWithNewData);

        const roomTable = service.getRoomTable(formGroup) as any;

        expect(roomTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoomTable for empty RoomTable initial value', () => {
        const formGroup = service.createRoomTableFormGroup();

        const roomTable = service.getRoomTable(formGroup) as any;

        expect(roomTable).toMatchObject({});
      });

      it('should return IRoomTable', () => {
        const formGroup = service.createRoomTableFormGroup(sampleWithRequiredData);

        const roomTable = service.getRoomTable(formGroup) as any;

        expect(roomTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoomTable should not enable id FormControl', () => {
        const formGroup = service.createRoomTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoomTable should disable id FormControl', () => {
        const formGroup = service.createRoomTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
