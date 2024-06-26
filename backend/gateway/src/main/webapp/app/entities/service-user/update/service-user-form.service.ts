import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IServiceUser, NewServiceUser } from '../service-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceUser for edit and NewServiceUserFormGroupInput for create.
 */
type ServiceUserFormGroupInput = IServiceUser | PartialWithRequiredKeyOf<NewServiceUser>;

type ServiceUserFormDefaults = Pick<NewServiceUser, 'id' | 'actifYN'>;

type ServiceUserFormGroupContent = {
  id: FormControl<IServiceUser['id'] | NewServiceUser['id']>;
  nom: FormControl<IServiceUser['nom']>;
  dateAjout: FormControl<IServiceUser['dateAjout']>;
  actifYN: FormControl<IServiceUser['actifYN']>;
};

export type ServiceUserFormGroup = FormGroup<ServiceUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceUserFormService {
  createServiceUserFormGroup(serviceUser: ServiceUserFormGroupInput = { id: null }): ServiceUserFormGroup {
    const serviceUserRawValue = {
      ...this.getFormDefaults(),
      ...serviceUser,
    };
    return new FormGroup<ServiceUserFormGroupContent>({
      id: new FormControl(
        { value: serviceUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(serviceUserRawValue.nom, {
        validators: [Validators.required],
      }),
      dateAjout: new FormControl(serviceUserRawValue.dateAjout, {
        validators: [Validators.required],
      }),
      actifYN: new FormControl(serviceUserRawValue.actifYN, {
        validators: [Validators.required],
      }),
    });
  }

  getServiceUser(form: ServiceUserFormGroup): IServiceUser | NewServiceUser {
    return form.getRawValue() as IServiceUser | NewServiceUser;
  }

  resetForm(form: ServiceUserFormGroup, serviceUser: ServiceUserFormGroupInput): void {
    const serviceUserRawValue = { ...this.getFormDefaults(), ...serviceUser };
    form.reset(
      {
        ...serviceUserRawValue,
        id: { value: serviceUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceUserFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
