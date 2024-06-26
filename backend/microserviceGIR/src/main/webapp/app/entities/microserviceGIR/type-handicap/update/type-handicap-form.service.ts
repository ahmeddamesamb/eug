import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeHandicap, NewTypeHandicap } from '../type-handicap.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeHandicap for edit and NewTypeHandicapFormGroupInput for create.
 */
type TypeHandicapFormGroupInput = ITypeHandicap | PartialWithRequiredKeyOf<NewTypeHandicap>;

type TypeHandicapFormDefaults = Pick<NewTypeHandicap, 'id'>;

type TypeHandicapFormGroupContent = {
  id: FormControl<ITypeHandicap['id'] | NewTypeHandicap['id']>;
  libelleTypeHandicap: FormControl<ITypeHandicap['libelleTypeHandicap']>;
};

export type TypeHandicapFormGroup = FormGroup<TypeHandicapFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeHandicapFormService {
  createTypeHandicapFormGroup(typeHandicap: TypeHandicapFormGroupInput = { id: null }): TypeHandicapFormGroup {
    const typeHandicapRawValue = {
      ...this.getFormDefaults(),
      ...typeHandicap,
    };
    return new FormGroup<TypeHandicapFormGroupContent>({
      id: new FormControl(
        { value: typeHandicapRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleTypeHandicap: new FormControl(typeHandicapRawValue.libelleTypeHandicap, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeHandicap(form: TypeHandicapFormGroup): ITypeHandicap | NewTypeHandicap {
    return form.getRawValue() as ITypeHandicap | NewTypeHandicap;
  }

  resetForm(form: TypeHandicapFormGroup, typeHandicap: TypeHandicapFormGroupInput): void {
    const typeHandicapRawValue = { ...this.getFormDefaults(), ...typeHandicap };
    form.reset(
      {
        ...typeHandicapRawValue,
        id: { value: typeHandicapRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeHandicapFormDefaults {
    return {
      id: null,
    };
  }
}
