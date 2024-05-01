import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRequestTable, NewRequestTable } from '../request-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRequestTable for edit and NewRequestTableFormGroupInput for create.
 */
type RequestTableFormGroupInput = IRequestTable | PartialWithRequiredKeyOf<NewRequestTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRequestTable | NewRequestTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

type RequestTableFormRawValue = FormValueOf<IRequestTable>;

type NewRequestTableFormRawValue = FormValueOf<NewRequestTable>;

type RequestTableFormDefaults = Pick<NewRequestTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt'>;

type RequestTableFormGroupContent = {
  id: FormControl<RequestTableFormRawValue['id'] | NewRequestTable['id']>;
  createAt: FormControl<RequestTableFormRawValue['createAt']>;
  updateAt: FormControl<RequestTableFormRawValue['updateAt']>;
  deletedAt: FormControl<RequestTableFormRawValue['deletedAt']>;
  status: FormControl<RequestTableFormRawValue['status']>;
  userId: FormControl<RequestTableFormRawValue['userId']>;
  title: FormControl<RequestTableFormRawValue['title']>;
  message: FormControl<RequestTableFormRawValue['message']>;
  reply: FormControl<RequestTableFormRawValue['reply']>;
  note: FormControl<RequestTableFormRawValue['note']>;
  notificationTable: FormControl<RequestTableFormRawValue['notificationTable']>;
};

export type RequestTableFormGroup = FormGroup<RequestTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RequestTableFormService {
  createRequestTableFormGroup(requestTable: RequestTableFormGroupInput = { id: null }): RequestTableFormGroup {
    const requestTableRawValue = this.convertRequestTableToRequestTableRawValue({
      ...this.getFormDefaults(),
      ...requestTable,
    });
    return new FormGroup<RequestTableFormGroupContent>({
      id: new FormControl(
        { value: requestTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(requestTableRawValue.createAt),
      updateAt: new FormControl(requestTableRawValue.updateAt),
      deletedAt: new FormControl(requestTableRawValue.deletedAt),
      status: new FormControl(requestTableRawValue.status),
      userId: new FormControl(requestTableRawValue.userId),
      title: new FormControl(requestTableRawValue.title),
      message: new FormControl(requestTableRawValue.message),
      reply: new FormControl(requestTableRawValue.reply),
      note: new FormControl(requestTableRawValue.note),
      notificationTable: new FormControl(requestTableRawValue.notificationTable),
    });
  }

  getRequestTable(form: RequestTableFormGroup): IRequestTable | NewRequestTable {
    return this.convertRequestTableRawValueToRequestTable(form.getRawValue() as RequestTableFormRawValue | NewRequestTableFormRawValue);
  }

  resetForm(form: RequestTableFormGroup, requestTable: RequestTableFormGroupInput): void {
    const requestTableRawValue = this.convertRequestTableToRequestTableRawValue({ ...this.getFormDefaults(), ...requestTable });
    form.reset(
      {
        ...requestTableRawValue,
        id: { value: requestTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RequestTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
    };
  }

  private convertRequestTableRawValueToRequestTable(
    rawRequestTable: RequestTableFormRawValue | NewRequestTableFormRawValue,
  ): IRequestTable | NewRequestTable {
    return {
      ...rawRequestTable,
      createAt: dayjs(rawRequestTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawRequestTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawRequestTable.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertRequestTableToRequestTableRawValue(
    requestTable: IRequestTable | (Partial<NewRequestTable> & RequestTableFormDefaults),
  ): RequestTableFormRawValue | PartialWithRequiredKeyOf<NewRequestTableFormRawValue> {
    return {
      ...requestTable,
      createAt: requestTable.createAt ? requestTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: requestTable.updateAt ? requestTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: requestTable.deletedAt ? requestTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
