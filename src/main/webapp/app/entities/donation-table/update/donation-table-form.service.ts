import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDonationTable, NewDonationTable } from '../donation-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDonationTable for edit and NewDonationTableFormGroupInput for create.
 */
type DonationTableFormGroupInput = IDonationTable | PartialWithRequiredKeyOf<NewDonationTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDonationTable | NewDonationTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

type DonationTableFormRawValue = FormValueOf<IDonationTable>;

type NewDonationTableFormRawValue = FormValueOf<NewDonationTable>;

type DonationTableFormDefaults = Pick<NewDonationTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt'>;

type DonationTableFormGroupContent = {
  id: FormControl<DonationTableFormRawValue['id'] | NewDonationTable['id']>;
  createAt: FormControl<DonationTableFormRawValue['createAt']>;
  updateAt: FormControl<DonationTableFormRawValue['updateAt']>;
  deletedAt: FormControl<DonationTableFormRawValue['deletedAt']>;
  donationId: FormControl<DonationTableFormRawValue['donationId']>;
  donationType: FormControl<DonationTableFormRawValue['donationType']>;
  donationDesc: FormControl<DonationTableFormRawValue['donationDesc']>;
  donationMonth: FormControl<DonationTableFormRawValue['donationMonth']>;
  donationCost: FormControl<DonationTableFormRawValue['donationCost']>;
  roomId: FormControl<DonationTableFormRawValue['roomId']>;
  status: FormControl<DonationTableFormRawValue['status']>;
  roomTable: FormControl<DonationTableFormRawValue['roomTable']>;
};

export type DonationTableFormGroup = FormGroup<DonationTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DonationTableFormService {
  createDonationTableFormGroup(donationTable: DonationTableFormGroupInput = { id: null }): DonationTableFormGroup {
    const donationTableRawValue = this.convertDonationTableToDonationTableRawValue({
      ...this.getFormDefaults(),
      ...donationTable,
    });
    return new FormGroup<DonationTableFormGroupContent>({
      id: new FormControl(
        { value: donationTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(donationTableRawValue.createAt),
      updateAt: new FormControl(donationTableRawValue.updateAt),
      deletedAt: new FormControl(donationTableRawValue.deletedAt),
      donationId: new FormControl(donationTableRawValue.donationId, {
        validators: [Validators.required],
      }),
      donationType: new FormControl(donationTableRawValue.donationType),
      donationDesc: new FormControl(donationTableRawValue.donationDesc),
      donationMonth: new FormControl(donationTableRawValue.donationMonth),
      donationCost: new FormControl(donationTableRawValue.donationCost),
      roomId: new FormControl(donationTableRawValue.roomId),
      status: new FormControl(donationTableRawValue.status),
      roomTable: new FormControl(donationTableRawValue.roomTable),
    });
  }

  getDonationTable(form: DonationTableFormGroup): IDonationTable | NewDonationTable {
    return this.convertDonationTableRawValueToDonationTable(form.getRawValue() as DonationTableFormRawValue | NewDonationTableFormRawValue);
  }

  resetForm(form: DonationTableFormGroup, donationTable: DonationTableFormGroupInput): void {
    const donationTableRawValue = this.convertDonationTableToDonationTableRawValue({ ...this.getFormDefaults(), ...donationTable });
    form.reset(
      {
        ...donationTableRawValue,
        id: { value: donationTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DonationTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
    };
  }

  private convertDonationTableRawValueToDonationTable(
    rawDonationTable: DonationTableFormRawValue | NewDonationTableFormRawValue,
  ): IDonationTable | NewDonationTable {
    return {
      ...rawDonationTable,
      createAt: dayjs(rawDonationTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawDonationTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawDonationTable.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertDonationTableToDonationTableRawValue(
    donationTable: IDonationTable | (Partial<NewDonationTable> & DonationTableFormDefaults),
  ): DonationTableFormRawValue | PartialWithRequiredKeyOf<NewDonationTableFormRawValue> {
    return {
      ...donationTable,
      createAt: donationTable.createAt ? donationTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: donationTable.updateAt ? donationTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: donationTable.deletedAt ? donationTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
