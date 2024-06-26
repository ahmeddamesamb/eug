import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInfosUser, NewInfosUser } from '../infos-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInfosUser for edit and NewInfosUserFormGroupInput for create.
 */
type InfosUserFormGroupInput = IInfosUser | PartialWithRequiredKeyOf<NewInfosUser>;

type InfosUserFormDefaults = Pick<NewInfosUser, 'id' | 'actifYN'>;

type InfosUserFormGroupContent = {
  id: FormControl<IInfosUser['id'] | NewInfosUser['id']>;
  dateAjout: FormControl<IInfosUser['dateAjout']>;
  actifYN: FormControl<IInfosUser['actifYN']>;
  user: FormControl<IInfosUser['user']>;
};

export type InfosUserFormGroup = FormGroup<InfosUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InfosUserFormService {
  createInfosUserFormGroup(infosUser: InfosUserFormGroupInput = { id: null }): InfosUserFormGroup {
    const infosUserRawValue = {
      ...this.getFormDefaults(),
      ...infosUser,
    };
    return new FormGroup<InfosUserFormGroupContent>({
      id: new FormControl(
        { value: infosUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateAjout: new FormControl(infosUserRawValue.dateAjout, {
        validators: [Validators.required],
      }),
      actifYN: new FormControl(infosUserRawValue.actifYN, {
        validators: [Validators.required],
      }),
      user: new FormControl(infosUserRawValue.user),
    });
  }

  getInfosUser(form: InfosUserFormGroup): IInfosUser | NewInfosUser {
    return form.getRawValue() as IInfosUser | NewInfosUser;
  }

  resetForm(form: InfosUserFormGroup, infosUser: InfosUserFormGroupInput): void {
    const infosUserRawValue = { ...this.getFormDefaults(), ...infosUser };
    form.reset(
      {
        ...infosUserRawValue,
        id: { value: infosUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InfosUserFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
