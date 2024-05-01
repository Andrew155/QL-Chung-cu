import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICitizenTable, NewCitizenTable } from '../citizen-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICitizenTable for edit and NewCitizenTableFormGroupInput for create.
 */
type CitizenTableFormGroupInput = ICitizenTable | PartialWithRequiredKeyOf<NewCitizenTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICitizenTable | NewCitizenTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

type CitizenTableFormRawValue = FormValueOf<ICitizenTable>;

type NewCitizenTableFormRawValue = FormValueOf<NewCitizenTable>;

type CitizenTableFormDefaults = Pick<NewCitizenTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt'>;

type CitizenTableFormGroupContent = {
  id: FormControl<CitizenTableFormRawValue['id'] | NewCitizenTable['id']>;
  createAt: FormControl<CitizenTableFormRawValue['createAt']>;
  updateAt: FormControl<CitizenTableFormRawValue['updateAt']>;
  deletedAt: FormControl<CitizenTableFormRawValue['deletedAt']>;
  citizenID: FormControl<CitizenTableFormRawValue['citizenID']>;
  name: FormControl<CitizenTableFormRawValue['name']>;
  dob: FormControl<CitizenTableFormRawValue['dob']>;
  contact: FormControl<CitizenTableFormRawValue['contact']>;
  gender: FormControl<CitizenTableFormRawValue['gender']>;
  relation: FormControl<CitizenTableFormRawValue['relation']>;
  status: FormControl<CitizenTableFormRawValue['status']>;
  familyId: FormControl<CitizenTableFormRawValue['familyId']>;
};

export type CitizenTableFormGroup = FormGroup<CitizenTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CitizenTableFormService {
  createCitizenTableFormGroup(citizenTable: CitizenTableFormGroupInput = { id: null }): CitizenTableFormGroup {
    const citizenTableRawValue = this.convertCitizenTableToCitizenTableRawValue({
      ...this.getFormDefaults(),
      ...citizenTable,
    });
    return new FormGroup<CitizenTableFormGroupContent>({
      id: new FormControl(
        { value: citizenTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(citizenTableRawValue.createAt),
      updateAt: new FormControl(citizenTableRawValue.updateAt),
      deletedAt: new FormControl(citizenTableRawValue.deletedAt),
      citizenID: new FormControl(citizenTableRawValue.citizenID, {
        validators: [Validators.required],
      }),
      name: new FormControl(citizenTableRawValue.name),
      dob: new FormControl(citizenTableRawValue.dob),
      contact: new FormControl(citizenTableRawValue.contact),
      gender: new FormControl(citizenTableRawValue.gender),
      relation: new FormControl(citizenTableRawValue.relation),
      status: new FormControl(citizenTableRawValue.status),
      familyId: new FormControl(citizenTableRawValue.familyId),
    });
  }

  getCitizenTable(form: CitizenTableFormGroup): ICitizenTable | NewCitizenTable {
    return this.convertCitizenTableRawValueToCitizenTable(form.getRawValue() as CitizenTableFormRawValue | NewCitizenTableFormRawValue);
  }

  resetForm(form: CitizenTableFormGroup, citizenTable: CitizenTableFormGroupInput): void {
    const citizenTableRawValue = this.convertCitizenTableToCitizenTableRawValue({ ...this.getFormDefaults(), ...citizenTable });
    form.reset(
      {
        ...citizenTableRawValue,
        id: { value: citizenTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CitizenTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
    };
  }

  private convertCitizenTableRawValueToCitizenTable(
    rawCitizenTable: CitizenTableFormRawValue | NewCitizenTableFormRawValue,
  ): ICitizenTable | NewCitizenTable {
    return {
      ...rawCitizenTable,
      createAt: dayjs(rawCitizenTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawCitizenTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawCitizenTable.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertCitizenTableToCitizenTableRawValue(
    citizenTable: ICitizenTable | (Partial<NewCitizenTable> & CitizenTableFormDefaults),
  ): CitizenTableFormRawValue | PartialWithRequiredKeyOf<NewCitizenTableFormRawValue> {
    return {
      ...citizenTable,
      createAt: citizenTable.createAt ? citizenTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: citizenTable.updateAt ? citizenTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: citizenTable.deletedAt ? citizenTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
