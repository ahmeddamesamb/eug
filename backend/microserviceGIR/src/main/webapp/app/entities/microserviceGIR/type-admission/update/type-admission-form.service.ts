import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeAdmission, NewTypeAdmission } from '../type-admission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeAdmission for edit and NewTypeAdmissionFormGroupInput for create.
 */
type TypeAdmissionFormGroupInput = ITypeAdmission | PartialWithRequiredKeyOf<NewTypeAdmission>;

type TypeAdmissionFormDefaults = Pick<NewTypeAdmission, 'id'>;

type TypeAdmissionFormGroupContent = {
  id: FormControl<ITypeAdmission['id'] | NewTypeAdmission['id']>;
  libelleTypeAdmission: FormControl<ITypeAdmission['libelleTypeAdmission']>;
};

export type TypeAdmissionFormGroup = FormGroup<TypeAdmissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeAdmissionFormService {
  createTypeAdmissionFormGroup(typeAdmission: TypeAdmissionFormGroupInput = { id: null }): TypeAdmissionFormGroup {
    const typeAdmissionRawValue = {
      ...this.getFormDefaults(),
      ...typeAdmission,
    };
    return new FormGroup<TypeAdmissionFormGroupContent>({
      id: new FormControl(
        { value: typeAdmissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleTypeAdmission: new FormControl(typeAdmissionRawValue.libelleTypeAdmission, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeAdmission(form: TypeAdmissionFormGroup): ITypeAdmission | NewTypeAdmission {
    return form.getRawValue() as ITypeAdmission | NewTypeAdmission;
  }

  resetForm(form: TypeAdmissionFormGroup, typeAdmission: TypeAdmissionFormGroupInput): void {
    const typeAdmissionRawValue = { ...this.getFormDefaults(), ...typeAdmission };
    form.reset(
      {
        ...typeAdmissionRawValue,
        id: { value: typeAdmissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeAdmissionFormDefaults {
    return {
      id: null,
    };
  }
}
