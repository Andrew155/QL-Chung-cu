import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IApplicationUser, NewApplicationUser } from '../application-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApplicationUser for edit and NewApplicationUserFormGroupInput for create.
 */
type ApplicationUserFormGroupInput = IApplicationUser | PartialWithRequiredKeyOf<NewApplicationUser>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IApplicationUser | NewApplicationUser> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

type ApplicationUserFormRawValue = FormValueOf<IApplicationUser>;

type NewApplicationUserFormRawValue = FormValueOf<NewApplicationUser>;

type ApplicationUserFormDefaults = Pick<NewApplicationUser, 'id' | 'createAt' | 'updateAt' | 'deletedAt'>;

type ApplicationUserFormGroupContent = {
  id: FormControl<ApplicationUserFormRawValue['id'] | NewApplicationUser['id']>;
  createAt: FormControl<ApplicationUserFormRawValue['createAt']>;
  updateAt: FormControl<ApplicationUserFormRawValue['updateAt']>;
  deletedAt: FormControl<ApplicationUserFormRawValue['deletedAt']>;
  familyId: FormControl<ApplicationUserFormRawValue['familyId']>;
  citizenID: FormControl<ApplicationUserFormRawValue['citizenID']>;
  name: FormControl<ApplicationUserFormRawValue['name']>;
  dob: FormControl<ApplicationUserFormRawValue['dob']>;
  contact: FormControl<ApplicationUserFormRawValue['contact']>;
  gender: FormControl<ApplicationUserFormRawValue['gender']>;
  relation: FormControl<ApplicationUserFormRawValue['relation']>;
  status: FormControl<ApplicationUserFormRawValue['status']>;
  roomId: FormControl<ApplicationUserFormRawValue['roomId']>;
  internalUser: FormControl<ApplicationUserFormRawValue['internalUser']>;
};

export type ApplicationUserFormGroup = FormGroup<ApplicationUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApplicationUserFormService {
  createApplicationUserFormGroup(applicationUser: ApplicationUserFormGroupInput = { id: null }): ApplicationUserFormGroup {
    const applicationUserRawValue = this.convertApplicationUserToApplicationUserRawValue({
      ...this.getFormDefaults(),
      ...applicationUser,
    });
    return new FormGroup<ApplicationUserFormGroupContent>({
      id: new FormControl(
        { value: applicationUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createAt: new FormControl(applicationUserRawValue.createAt),
      updateAt: new FormControl(applicationUserRawValue.updateAt),
      deletedAt: new FormControl(applicationUserRawValue.deletedAt),
      familyId: new FormControl(applicationUserRawValue.familyId),
      citizenID: new FormControl(applicationUserRawValue.citizenID, {
        validators: [Validators.required],
      }),
      name: new FormControl(applicationUserRawValue.name),
      dob: new FormControl(applicationUserRawValue.dob),
      contact: new FormControl(applicationUserRawValue.contact),
      gender: new FormControl(applicationUserRawValue.gender),
      relation: new FormControl(applicationUserRawValue.relation),
      status: new FormControl(applicationUserRawValue.status),
      roomId: new FormControl(applicationUserRawValue.roomId),
      internalUser: new FormControl(applicationUserRawValue.internalUser),
    });
  }

  getApplicationUser(form: ApplicationUserFormGroup): IApplicationUser | NewApplicationUser {
    return this.convertApplicationUserRawValueToApplicationUser(
      form.getRawValue() as ApplicationUserFormRawValue | NewApplicationUserFormRawValue,
    );
  }

  resetForm(form: ApplicationUserFormGroup, applicationUser: ApplicationUserFormGroupInput): void {
    const applicationUserRawValue = this.convertApplicationUserToApplicationUserRawValue({ ...this.getFormDefaults(), ...applicationUser });
    form.reset(
      {
        ...applicationUserRawValue,
        id: { value: applicationUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApplicationUserFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createAt: currentTime,
      updateAt: currentTime,
      deletedAt: currentTime,
    };
  }

  private convertApplicationUserRawValueToApplicationUser(
    rawApplicationUser: ApplicationUserFormRawValue | NewApplicationUserFormRawValue,
  ): IApplicationUser | NewApplicationUser {
    return {
      ...rawApplicationUser,
      createAt: dayjs(rawApplicationUser.createAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawApplicationUser.updateAt, DATE_TIME_FORMAT),
      deletedAt: dayjs(rawApplicationUser.deletedAt, DATE_TIME_FORMAT),
    };
  }

  private convertApplicationUserToApplicationUserRawValue(
    applicationUser: IApplicationUser | (Partial<NewApplicationUser> & ApplicationUserFormDefaults),
  ): ApplicationUserFormRawValue | PartialWithRequiredKeyOf<NewApplicationUserFormRawValue> {
    return {
      ...applicationUser,
      createAt: applicationUser.createAt ? applicationUser.createAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: applicationUser.updateAt ? applicationUser.updateAt.format(DATE_TIME_FORMAT) : undefined,
      deletedAt: applicationUser.deletedAt ? applicationUser.deletedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
