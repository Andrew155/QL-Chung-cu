import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificationTable, NewNotificationTable } from '../notification-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificationTable for edit and NewNotificationTableFormGroupInput for create.
 */
type NotificationTableFormGroupInput = INotificationTable | PartialWithRequiredKeyOf<NewNotificationTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotificationTable | NewNotificationTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

type NotificationTableFormRawValue = FormValueOf<INotificationTable>;

type NewNotificationTableFormRawValue = FormValueOf<NewNotificationTable>;

type NotificationTableFormDefaults = Pick<NewNotificationTable, 'id' | 'createAt' | 'updateAt' | 'deletedAt'>;

type NotificationTableFormGroupContent = {
  id: FormControl<NotificationTableFormRawValue['id'] | NewNotificationTable['id']>;
  createAt: FormControl<NotificationTableFormRawValue['createAt']>;
  updateAt: FormControl<NotificationTableFormRawValue['updateAt']>;
  deletedAt: FormControl<NotificationTableFormRawValue['deletedAt']>;
  title: FormControl<NotificationTableFormRawValue['title']>;
  content: FormControl<NotificationTableFormRawValue['content']>;
  userID: FormControl<NotificationTableFormRawValue['userID']>;
  applicationTable: FormControl<NotificationTableFormRawValue['applicationTable']>;
};

export type NotificationTableFormGroup = FormGroup<NotificationTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationTableFormService {
  createNotificationTableFormGroup(notificationTable: NotificationTableFormGroupInput = { id: null }): NotificationTableFormGroup {
    const notificationTableRawValue = this.convertNotificationTableToNotificationTableRawValue({
      ...this.getFormDefaults(),
      ...notificationTable,
    });
    return new FormGroup<NotificationTableFormGroupContent>({
      id: new FormControl(
        { value: notificationTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(notificationTableRawValue.createAt),
      updateAt: new FormControl(notificationTableRawValue.updateAt),
      deletedAt: new FormControl(notificationTableRawValue.deletedAt),
      title: new FormControl(notificationTableRawValue.title),
      content: new FormControl(notificationTableRawValue.content),
      userID: new FormControl(notificationTableRawValue.userID),
      applicationTable: new FormControl(notificationTableRawValue.applicationTable),
    });
  }

  getNotificationTable(form: NotificationTableFormGroup): INotificationTable | NewNotificationTable {
    return this.convertNotificationTableRawValueToNotificationTable(
      form.getRawValue() as NotificationTableFormRawValue | NewNotificationTableFormRawValue,
    );
  }

  resetForm(form: NotificationTableFormGroup, notificationTable: NotificationTableFormGroupInput): void {
    const notificationTableRawValue = this.convertNotificationTableToNotificationTableRawValue({
      ...this.getFormDefaults(),
      ...notificationTable,
    });
    form.reset(
      {
        ...notificationTableRawValue,
        id: { value: notificationTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
    };
  }

  private convertNotificationTableRawValueToNotificationTable(
    rawNotificationTable: NotificationTableFormRawValue | NewNotificationTableFormRawValue,
  ): INotificationTable | NewNotificationTable {
    return {
      ...rawNotificationTable,
      createAt: dayjs(rawNotificationTable.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawNotificationTable.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawNotificationTable.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationTableToNotificationTableRawValue(
    notificationTable: INotificationTable | (Partial<NewNotificationTable> & NotificationTableFormDefaults),
  ): NotificationTableFormRawValue | PartialWithRequiredKeyOf<NewNotificationTableFormRawValue> {
    return {
      ...notificationTable,
      createAt: notificationTable.createAt ? notificationTable.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: notificationTable.updateAt ? notificationTable.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: notificationTable.deletedAt ? notificationTable.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
