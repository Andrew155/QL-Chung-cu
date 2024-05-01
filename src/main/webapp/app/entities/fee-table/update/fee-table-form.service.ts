import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeeTable, NewFeeTable } from '../fee-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeeTable for edit and NewFeeTableFormGroupInput for create.
 */
type FeeTableFormGroupInput = IFeeTable | PartialWithRequiredKeyOf<NewFeeTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFeeTable | NewFeeTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt' | 'date'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
  date?: string | null;
};

type FeeTableFormRawValue = FormValueOf<IFeeTable>;

type NewFeeTableFormRawValue = FormValueOf<NewFeeTable>;

type FeeTableFormDefaults = Pick<NewFeeTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt' | 'date'>;

type FeeTableFormGroupContent = {
  id: FormControl<FeeTableFormRawValue['id'] | NewFeeTable['id']>;
  createAt: FormControl<FeeTableFormRawValue['createAt']>;
  updateAt: FormControl<FeeTableFormRawValue['updateAt']>;
  deletedAt: FormControl<FeeTableFormRawValue['deletedAt']>;
  feeType: FormControl<FeeTableFormRawValue['feeType']>;
  feeDesc: FormControl<FeeTableFormRawValue['feeDesc']>;
  feeMonth: FormControl<FeeTableFormRawValue['feeMonth']>;
  feeCost: FormControl<FeeTableFormRawValue['feeCost']>;
  date: FormControl<FeeTableFormRawValue['date']>;
  status: FormControl<FeeTableFormRawValue['status']>;
  feeId: FormControl<FeeTableFormRawValue['feeId']>;
  roomTable: FormControl<FeeTableFormRawValue['roomTable']>;
};

export type FeeTableFormGroup = FormGroup<FeeTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeeTableFormService {
  createFeeTableFormGroup(feeTable: FeeTableFormGroupInput = { id: null }): FeeTableFormGroup {
    const feeTableRawValue = this.convertFeeTableToFeeTableRawValue({
      ...this.getFormDefaults(),
      ...feeTable,
    });
    return new FormGroup<FeeTableFormGroupContent>({
      id: new FormControl(
        { value: feeTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(feeTableRawValue.createAt),
      updateAt: new FormControl(feeTableRawValue.updateAt),
      deletedAt: new FormControl(feeTableRawValue.deletedAt),
      feeType: new FormControl(feeTableRawValue.feeType),
      feeDesc: new FormControl(feeTableRawValue.feeDesc),
      feeMonth: new FormControl(feeTableRawValue.feeMonth),
      feeCost: new FormControl(feeTableRawValue.feeCost),
      date: new FormControl(feeTableRawValue.date),
      status: new FormControl(feeTableRawValue.status),
      feeId: new FormControl(feeTableRawValue.feeId, {
        validators: [Validators.required],
      }),
      roomTable: new FormControl(feeTableRawValue.roomTable),
    });
  }

  getFeeTable(form: FeeTableFormGroup): IFeeTable | NewFeeTable {
    return this.convertFeeTableRawValueToFeeTable(form.getRawValue() as FeeTableFormRawValue | NewFeeTableFormRawValue);
  }

  resetForm(form: FeeTableFormGroup, feeTable: FeeTableFormGroupInput): void {
    const feeTableRawValue = this.convertFeeTableToFeeTableRawValue({ ...this.getFormDefaults(), ...feeTable });
    form.reset(
      {
        ...feeTableRawValue,
        id: { value: feeTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeeTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
      date: currentTime,
    };
  }

  private convertFeeTableRawValueToFeeTable(rawFeeTable: FeeTableFormRawValue | NewFeeTableFormRawValue): IFeeTable | NewFeeTable {
    return {
      ...rawFeeTable,
      createAt: dayjs(rawFeeTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawFeeTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawFeeTable.deletedAt, DATE_TIME_FORMAT),
      date: dayjs(rawFeeTable.date, DATE_TIME_FORMAT),
    };
  }

  private convertFeeTableToFeeTableRawValue(
    feeTable: IFeeTable | (Partial<NewFeeTable> & FeeTableFormDefaults),
  ): FeeTableFormRawValue | PartialWithRequiredKeyOf<NewFeeTableFormRawValue> {
    return {
      ...feeTable,
      createAt: feeTable.createAt ? feeTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: feeTable.updateAt ? feeTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: feeTable.deletedAt ? feeTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
      date: feeTable.date ? feeTable.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
