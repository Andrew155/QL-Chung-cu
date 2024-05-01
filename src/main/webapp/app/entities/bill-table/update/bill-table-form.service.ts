import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBillTable, NewBillTable } from '../bill-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBillTable for edit and NewBillTableFormGroupInput for create.
 */
type BillTableFormGroupInput = IBillTable | PartialWithRequiredKeyOf<NewBillTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBillTable | NewBillTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt' | 'date'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
  date?: string | null;
};

type BillTableFormRawValue = FormValueOf<IBillTable>;

type NewBillTableFormRawValue = FormValueOf<NewBillTable>;

type BillTableFormDefaults = Pick<NewBillTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt' | 'date'>;

type BillTableFormGroupContent = {
  id: FormControl<BillTableFormRawValue['id'] | NewBillTable['id']>;
  createAt: FormControl<BillTableFormRawValue['createAt']>;
  updateAt: FormControl<BillTableFormRawValue['updateAt']>;
  deletedAt: FormControl<BillTableFormRawValue['deletedAt']>;
  billType: FormControl<BillTableFormRawValue['billType']>;
  billId: FormControl<BillTableFormRawValue['billId']>;
  billMonth: FormControl<BillTableFormRawValue['billMonth']>;
  billAmount: FormControl<BillTableFormRawValue['billAmount']>;
  roomId: FormControl<BillTableFormRawValue['roomId']>;
  date: FormControl<BillTableFormRawValue['date']>;
  status: FormControl<BillTableFormRawValue['status']>;
  billCost: FormControl<BillTableFormRawValue['billCost']>;
  customerID: FormControl<BillTableFormRawValue['customerID']>;
  roomTable: FormControl<BillTableFormRawValue['roomTable']>;
  applicationTable: FormControl<BillTableFormRawValue['applicationTable']>;
};

export type BillTableFormGroup = FormGroup<BillTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BillTableFormService {
  createBillTableFormGroup(billTable: BillTableFormGroupInput = { id: null }): BillTableFormGroup {
    const billTableRawValue = this.convertBillTableToBillTableRawValue({
      ...this.getFormDefaults(),
      ...billTable,
    });
    return new FormGroup<BillTableFormGroupContent>({
      id: new FormControl(
        { value: billTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(billTableRawValue.createAt),
      updateAt: new FormControl(billTableRawValue.updateAt),
      deletedAt: new FormControl(billTableRawValue.deletedAt),
      billType: new FormControl(billTableRawValue.billType),
      billId: new FormControl(billTableRawValue.billId, {
        validators: [Validators.required],
      }),
      billMonth: new FormControl(billTableRawValue.billMonth),
      billAmount: new FormControl(billTableRawValue.billAmount),
      roomId: new FormControl(billTableRawValue.roomId),
      date: new FormControl(billTableRawValue.date),
      status: new FormControl(billTableRawValue.status),
      billCost: new FormControl(billTableRawValue.billCost),
      customerID: new FormControl(billTableRawValue.customerID),
      roomTable: new FormControl(billTableRawValue.roomTable),
      applicationTable: new FormControl(billTableRawValue.applicationTable),
    });
  }

  getBillTable(form: BillTableFormGroup): IBillTable | NewBillTable {
    return this.convertBillTableRawValueToBillTable(form.getRawValue() as BillTableFormRawValue | NewBillTableFormRawValue);
  }

  resetForm(form: BillTableFormGroup, billTable: BillTableFormGroupInput): void {
    const billTableRawValue = this.convertBillTableToBillTableRawValue({ ...this.getFormDefaults(), ...billTable });
    form.reset(
      {
        ...billTableRawValue,
        id: { value: billTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BillTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
      date: currentTime,
    };
  }

  private convertBillTableRawValueToBillTable(rawBillTable: BillTableFormRawValue | NewBillTableFormRawValue): IBillTable | NewBillTable {
    return {
      ...rawBillTable,
      createAt: dayjs(rawBillTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawBillTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawBillTable.deletedAt, DATE_TIME_FORMAT),
      date: dayjs(rawBillTable.date, DATE_TIME_FORMAT),
    };
  }

  private convertBillTableToBillTableRawValue(
    billTable: IBillTable | (Partial<NewBillTable> & BillTableFormDefaults),
  ): BillTableFormRawValue | PartialWithRequiredKeyOf<NewBillTableFormRawValue> {
    return {
      ...billTable,
      createAt: billTable.createAt ? billTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: billTable.updateAt ? billTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: billTable.deletedAt ? billTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
      date: billTable.date ? billTable.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
