import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRoomTable, NewRoomTable } from '../room-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoomTable for edit and NewRoomTableFormGroupInput for create.
 */
type RoomTableFormGroupInput = IRoomTable | PartialWithRequiredKeyOf<NewRoomTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRoomTable | NewRoomTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

type RoomTableFormRawValue = FormValueOf<IRoomTable>;

type NewRoomTableFormRawValue = FormValueOf<NewRoomTable>;

type RoomTableFormDefaults = Pick<NewRoomTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt'>;

type RoomTableFormGroupContent = {
  id: FormControl<RoomTableFormRawValue['id'] | NewRoomTable['id']>;
  createAt: FormControl<RoomTableFormRawValue['createAt']>;
  updateAt: FormControl<RoomTableFormRawValue['updateAt']>;
  deletedAt: FormControl<RoomTableFormRawValue['deletedAt']>;
  roomId: FormControl<RoomTableFormRawValue['roomId']>;
  area: FormControl<RoomTableFormRawValue['area']>;
  ownTime: FormControl<RoomTableFormRawValue['ownTime']>;
  ownerId: FormControl<RoomTableFormRawValue['ownerId']>;
  ownerName: FormControl<RoomTableFormRawValue['ownerName']>;
  status: FormControl<RoomTableFormRawValue['status']>;
};

export type RoomTableFormGroup = FormGroup<RoomTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomTableFormService {
  createRoomTableFormGroup(roomTable: RoomTableFormGroupInput = { id: null }): RoomTableFormGroup {
    const roomTableRawValue = this.convertRoomTableToRoomTableRawValue({
      ...this.getFormDefaults(),
      ...roomTable,
    });
    return new FormGroup<RoomTableFormGroupContent>({
      id: new FormControl(
        { value: roomTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(roomTableRawValue.createAt),
      updateAt: new FormControl(roomTableRawValue.updateAt),
      deletedAt: new FormControl(roomTableRawValue.deletedAt),
      roomId: new FormControl(roomTableRawValue.roomId, {
        validators: [Validators.required],
      }),
      area: new FormControl(roomTableRawValue.area),
      ownTime: new FormControl(roomTableRawValue.ownTime),
      ownerId: new FormControl(roomTableRawValue.ownerId),
      ownerName: new FormControl(roomTableRawValue.ownerName),
      status: new FormControl(roomTableRawValue.status),
    });
  }

  getRoomTable(form: RoomTableFormGroup): IRoomTable | NewRoomTable {
    return this.convertRoomTableRawValueToRoomTable(form.getRawValue() as RoomTableFormRawValue | NewRoomTableFormRawValue);
  }

  resetForm(form: RoomTableFormGroup, roomTable: RoomTableFormGroupInput): void {
    const roomTableRawValue = this.convertRoomTableToRoomTableRawValue({ ...this.getFormDefaults(), ...roomTable });
    form.reset(
      {
        ...roomTableRawValue,
        id: { value: roomTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RoomTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
    };
  }

  private convertRoomTableRawValueToRoomTable(rawRoomTable: RoomTableFormRawValue | NewRoomTableFormRawValue): IRoomTable | NewRoomTable {
    return {
      ...rawRoomTable,
      createAt: dayjs(rawRoomTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawRoomTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawRoomTable.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertRoomTableToRoomTableRawValue(
    roomTable: IRoomTable | (Partial<NewRoomTable> & RoomTableFormDefaults),
  ): RoomTableFormRawValue | PartialWithRequiredKeyOf<NewRoomTableFormRawValue> {
    return {
      ...roomTable,
      createAt: roomTable.createAt ? roomTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: roomTable.updateAt ? roomTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: roomTable.deletedAt ? roomTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
