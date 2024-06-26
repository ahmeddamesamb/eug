import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserProfile, NewUserProfile } from '../user-profile.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserProfile for edit and NewUserProfileFormGroupInput for create.
 */
type UserProfileFormGroupInput = IUserProfile | PartialWithRequiredKeyOf<NewUserProfile>;

type UserProfileFormDefaults = Pick<NewUserProfile, 'id' | 'enCoursYN'>;

type UserProfileFormGroupContent = {
  id: FormControl<IUserProfile['id'] | NewUserProfile['id']>;
  dateDebut: FormControl<IUserProfile['dateDebut']>;
  dateFin: FormControl<IUserProfile['dateFin']>;
  enCoursYN: FormControl<IUserProfile['enCoursYN']>;
  profil: FormControl<IUserProfile['profil']>;
  infoUser: FormControl<IUserProfile['infoUser']>;
};

export type UserProfileFormGroup = FormGroup<UserProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserProfileFormService {
  createUserProfileFormGroup(userProfile: UserProfileFormGroupInput = { id: null }): UserProfileFormGroup {
    const userProfileRawValue = {
      ...this.getFormDefaults(),
      ...userProfile,
    };
    return new FormGroup<UserProfileFormGroupContent>({
      id: new FormControl(
        { value: userProfileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDebut: new FormControl(userProfileRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(userProfileRawValue.dateFin, {
        validators: [Validators.required],
      }),
      enCoursYN: new FormControl(userProfileRawValue.enCoursYN, {
        validators: [Validators.required],
      }),
      profil: new FormControl(userProfileRawValue.profil),
      infoUser: new FormControl(userProfileRawValue.infoUser),
    });
  }

  getUserProfile(form: UserProfileFormGroup): IUserProfile | NewUserProfile {
    return form.getRawValue() as IUserProfile | NewUserProfile;
  }

  resetForm(form: UserProfileFormGroup, userProfile: UserProfileFormGroupInput): void {
    const userProfileRawValue = { ...this.getFormDefaults(), ...userProfile };
    form.reset(
      {
        ...userProfileRawValue,
        id: { value: userProfileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserProfileFormDefaults {
    return {
      id: null,
      enCoursYN: false,
    };
  }
}
