import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserProfileBlocFonctionnel, NewUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserProfileBlocFonctionnel for edit and NewUserProfileBlocFonctionnelFormGroupInput for create.
 */
type UserProfileBlocFonctionnelFormGroupInput = IUserProfileBlocFonctionnel | PartialWithRequiredKeyOf<NewUserProfileBlocFonctionnel>;

type UserProfileBlocFonctionnelFormDefaults = Pick<NewUserProfileBlocFonctionnel, 'id' | 'enCoursYN'>;

type UserProfileBlocFonctionnelFormGroupContent = {
  id: FormControl<IUserProfileBlocFonctionnel['id'] | NewUserProfileBlocFonctionnel['id']>;
  date: FormControl<IUserProfileBlocFonctionnel['date']>;
  enCoursYN: FormControl<IUserProfileBlocFonctionnel['enCoursYN']>;
  userProfil: FormControl<IUserProfileBlocFonctionnel['userProfil']>;
  blocFonctionnel: FormControl<IUserProfileBlocFonctionnel['blocFonctionnel']>;
};

export type UserProfileBlocFonctionnelFormGroup = FormGroup<UserProfileBlocFonctionnelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserProfileBlocFonctionnelFormService {
  createUserProfileBlocFonctionnelFormGroup(
    userProfileBlocFonctionnel: UserProfileBlocFonctionnelFormGroupInput = { id: null },
  ): UserProfileBlocFonctionnelFormGroup {
    const userProfileBlocFonctionnelRawValue = {
      ...this.getFormDefaults(),
      ...userProfileBlocFonctionnel,
    };
    return new FormGroup<UserProfileBlocFonctionnelFormGroupContent>({
      id: new FormControl(
        { value: userProfileBlocFonctionnelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(userProfileBlocFonctionnelRawValue.date, {
        validators: [Validators.required],
      }),
      enCoursYN: new FormControl(userProfileBlocFonctionnelRawValue.enCoursYN, {
        validators: [Validators.required],
      }),
      userProfil: new FormControl(userProfileBlocFonctionnelRawValue.userProfil),
      blocFonctionnel: new FormControl(userProfileBlocFonctionnelRawValue.blocFonctionnel),
    });
  }

  getUserProfileBlocFonctionnel(form: UserProfileBlocFonctionnelFormGroup): IUserProfileBlocFonctionnel | NewUserProfileBlocFonctionnel {
    return form.getRawValue() as IUserProfileBlocFonctionnel | NewUserProfileBlocFonctionnel;
  }

  resetForm(form: UserProfileBlocFonctionnelFormGroup, userProfileBlocFonctionnel: UserProfileBlocFonctionnelFormGroupInput): void {
    const userProfileBlocFonctionnelRawValue = { ...this.getFormDefaults(), ...userProfileBlocFonctionnel };
    form.reset(
      {
        ...userProfileBlocFonctionnelRawValue,
        id: { value: userProfileBlocFonctionnelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserProfileBlocFonctionnelFormDefaults {
    return {
      id: null,
      enCoursYN: false,
    };
  }
}
