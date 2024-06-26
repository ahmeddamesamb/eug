import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeFormation, NewTypeFormation } from '../type-formation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeFormation for edit and NewTypeFormationFormGroupInput for create.
 */
type TypeFormationFormGroupInput = ITypeFormation | PartialWithRequiredKeyOf<NewTypeFormation>;

type TypeFormationFormDefaults = Pick<NewTypeFormation, 'id'>;

type TypeFormationFormGroupContent = {
  id: FormControl<ITypeFormation['id'] | NewTypeFormation['id']>;
  libelleTypeFormation: FormControl<ITypeFormation['libelleTypeFormation']>;
};

export type TypeFormationFormGroup = FormGroup<TypeFormationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeFormationFormService {
  createTypeFormationFormGroup(typeFormation: TypeFormationFormGroupInput = { id: null }): TypeFormationFormGroup {
    const typeFormationRawValue = {
      ...this.getFormDefaults(),
      ...typeFormation,
    };
    return new FormGroup<TypeFormationFormGroupContent>({
      id: new FormControl(
        { value: typeFormationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleTypeFormation: new FormControl(typeFormationRawValue.libelleTypeFormation, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeFormation(form: TypeFormationFormGroup): ITypeFormation | NewTypeFormation {
    return form.getRawValue() as ITypeFormation | NewTypeFormation;
  }

  resetForm(form: TypeFormationFormGroup, typeFormation: TypeFormationFormGroupInput): void {
    const typeFormationRawValue = { ...this.getFormDefaults(), ...typeFormation };
    form.reset(
      {
        ...typeFormationRawValue,
        id: { value: typeFormationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeFormationFormDefaults {
    return {
      id: null,
    };
  }
}
