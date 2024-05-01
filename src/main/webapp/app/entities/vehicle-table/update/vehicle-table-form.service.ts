import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVehicleTable, NewVehicleTable } from '../vehicle-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicleTable for edit and NewVehicleTableFormGroupInput for create.
 */
type VehicleTableFormGroupInput = IVehicleTable | PartialWithRequiredKeyOf<NewVehicleTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVehicleTable | NewVehicleTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

type VehicleTableFormRawValue = FormValueOf<IVehicleTable>;

type NewVehicleTableFormRawValue = FormValueOf<NewVehicleTable>;

type VehicleTableFormDefaults = Pick<NewVehicleTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt'>;

type VehicleTableFormGroupContent = {
  id: FormControl<VehicleTableFormRawValue['id'] | NewVehicleTable['id']>;
  createAt: FormControl<VehicleTableFormRawValue['createAt']>;
  updateAt: FormControl<VehicleTableFormRawValue['updateAt']>;
  deletedAt: FormControl<VehicleTableFormRawValue['deletedAt']>;
  vehicleName: FormControl<VehicleTableFormRawValue['vehicleName']>;
  vehicleType: FormControl<VehicleTableFormRawValue['vehicleType']>;
  vehicleId: FormControl<VehicleTableFormRawValue['vehicleId']>;
  roomId: FormControl<VehicleTableFormRawValue['roomId']>;
  ownerId: FormControl<VehicleTableFormRawValue['ownerId']>;
  vehicleFee: FormControl<VehicleTableFormRawValue['vehicleFee']>;
  roomTable: FormControl<VehicleTableFormRawValue['roomTable']>;
};

export type VehicleTableFormGroup = FormGroup<VehicleTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleTableFormService {
  createVehicleTableFormGroup(vehicleTable: VehicleTableFormGroupInput = { id: null }): VehicleTableFormGroup {
    const vehicleTableRawValue = this.convertVehicleTableToVehicleTableRawValue({
      ...this.getFormDefaults(),
      ...vehicleTable,
    });
    return new FormGroup<VehicleTableFormGroupContent>({
      id: new FormControl(
        { value: vehicleTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(vehicleTableRawValue.createAt),
      updateAt: new FormControl(vehicleTableRawValue.updateAt),
      deletedAt: new FormControl(vehicleTableRawValue.deletedAt),
      vehicleName: new FormControl(vehicleTableRawValue.vehicleName),
      vehicleType: new FormControl(vehicleTableRawValue.vehicleType),
      vehicleId: new FormControl(vehicleTableRawValue.vehicleId, {
        validators: [Validators.required],
      }),
      roomId: new FormControl(vehicleTableRawValue.roomId),
      ownerId: new FormControl(vehicleTableRawValue.ownerId),
      vehicleFee: new FormControl(vehicleTableRawValue.vehicleFee),
      roomTable: new FormControl(vehicleTableRawValue.roomTable),
    });
  }

  getVehicleTable(form: VehicleTableFormGroup): IVehicleTable | NewVehicleTable {
    return this.convertVehicleTableRawValueToVehicleTable(form.getRawValue() as VehicleTableFormRawValue | NewVehicleTableFormRawValue);
  }

  resetForm(form: VehicleTableFormGroup, vehicleTable: VehicleTableFormGroupInput): void {
    const vehicleTableRawValue = this.convertVehicleTableToVehicleTableRawValue({ ...this.getFormDefaults(), ...vehicleTable });
    form.reset(
      {
        ...vehicleTableRawValue,
        id: { value: vehicleTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
    };
  }

  private convertVehicleTableRawValueToVehicleTable(
    rawVehicleTable: VehicleTableFormRawValue | NewVehicleTableFormRawValue,
  ): IVehicleTable | NewVehicleTable {
    return {
      ...rawVehicleTable,
      createAt: dayjs(rawVehicleTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawVehicleTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawVehicleTable.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertVehicleTableToVehicleTableRawValue(
    vehicleTable: IVehicleTable | (Partial<NewVehicleTable> & VehicleTableFormDefaults),
  ): VehicleTableFormRawValue | PartialWithRequiredKeyOf<NewVehicleTableFormRawValue> {
    return {
      ...vehicleTable,
      createAt: vehicleTable.createAt ? vehicleTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: vehicleTable.updateAt ? vehicleTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: vehicleTable.deletedAt ? vehicleTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
