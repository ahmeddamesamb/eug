import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOperateur, NewOperateur } from '../operateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperateur for edit and NewOperateurFormGroupInput for create.
 */
type OperateurFormGroupInput = IOperateur | PartialWithRequiredKeyOf<NewOperateur>;

type OperateurFormDefaults = Pick<NewOperateur, 'id'>;

type OperateurFormGroupContent = {
  id: FormControl<IOperateur['id'] | NewOperateur['id']>;
  libelleOperateur: FormControl<IOperateur['libelleOperateur']>;
  userLogin: FormControl<IOperateur['userLogin']>;
  codeOperateur: FormControl<IOperateur['codeOperateur']>;
};

export type OperateurFormGroup = FormGroup<OperateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperateurFormService {
  createOperateurFormGroup(operateur: OperateurFormGroupInput = { id: null }): OperateurFormGroup {
    const operateurRawValue = {
      ...this.getFormDefaults(),
      ...operateur,
    };
    return new FormGroup<OperateurFormGroupContent>({
      id: new FormControl(
        { value: operateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleOperateur: new FormControl(operateurRawValue.libelleOperateur, {
        validators: [Validators.required],
      }),
      userLogin: new FormControl(operateurRawValue.userLogin, {
        validators: [Validators.required],
      }),
      codeOperateur: new FormControl(operateurRawValue.codeOperateur, {
        validators: [Validators.required],
      }),
    });
  }

  getOperateur(form: OperateurFormGroup): IOperateur | NewOperateur {
    return form.getRawValue() as IOperateur | NewOperateur;
  }

  resetForm(form: OperateurFormGroup, operateur: OperateurFormGroupInput): void {
    const operateurRawValue = { ...this.getFormDefaults(), ...operateur };
    form.reset(
      {
        ...operateurRawValue,
        id: { value: operateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OperateurFormDefaults {
    return {
      id: null,
    };
  }
}
