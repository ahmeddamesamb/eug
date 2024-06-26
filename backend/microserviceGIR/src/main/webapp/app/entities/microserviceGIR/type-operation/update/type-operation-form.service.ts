import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeOperation, NewTypeOperation } from '../type-operation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeOperation for edit and NewTypeOperationFormGroupInput for create.
 */
type TypeOperationFormGroupInput = ITypeOperation | PartialWithRequiredKeyOf<NewTypeOperation>;

type TypeOperationFormDefaults = Pick<NewTypeOperation, 'id'>;

type TypeOperationFormGroupContent = {
  id: FormControl<ITypeOperation['id'] | NewTypeOperation['id']>;
  libelleTypeOperation: FormControl<ITypeOperation['libelleTypeOperation']>;
};

export type TypeOperationFormGroup = FormGroup<TypeOperationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeOperationFormService {
  createTypeOperationFormGroup(typeOperation: TypeOperationFormGroupInput = { id: null }): TypeOperationFormGroup {
    const typeOperationRawValue = {
      ...this.getFormDefaults(),
      ...typeOperation,
    };
    return new FormGroup<TypeOperationFormGroupContent>({
      id: new FormControl(
        { value: typeOperationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleTypeOperation: new FormControl(typeOperationRawValue.libelleTypeOperation, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeOperation(form: TypeOperationFormGroup): ITypeOperation | NewTypeOperation {
    return form.getRawValue() as ITypeOperation | NewTypeOperation;
  }

  resetForm(form: TypeOperationFormGroup, typeOperation: TypeOperationFormGroupInput): void {
    const typeOperationRawValue = { ...this.getFormDefaults(), ...typeOperation };
    form.reset(
      {
        ...typeOperationRawValue,
        id: { value: typeOperationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeOperationFormDefaults {
    return {
      id: null,
    };
  }
}
