import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeBourse, NewTypeBourse } from '../type-bourse.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeBourse for edit and NewTypeBourseFormGroupInput for create.
 */
type TypeBourseFormGroupInput = ITypeBourse | PartialWithRequiredKeyOf<NewTypeBourse>;

type TypeBourseFormDefaults = Pick<NewTypeBourse, 'id'>;

type TypeBourseFormGroupContent = {
  id: FormControl<ITypeBourse['id'] | NewTypeBourse['id']>;
  libelle: FormControl<ITypeBourse['libelle']>;
};

export type TypeBourseFormGroup = FormGroup<TypeBourseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeBourseFormService {
  createTypeBourseFormGroup(typeBourse: TypeBourseFormGroupInput = { id: null }): TypeBourseFormGroup {
    const typeBourseRawValue = {
      ...this.getFormDefaults(),
      ...typeBourse,
    };
    return new FormGroup<TypeBourseFormGroupContent>({
      id: new FormControl(
        { value: typeBourseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelle: new FormControl(typeBourseRawValue.libelle),
    });
  }

  getTypeBourse(form: TypeBourseFormGroup): ITypeBourse | NewTypeBourse {
    return form.getRawValue() as ITypeBourse | NewTypeBourse;
  }

  resetForm(form: TypeBourseFormGroup, typeBourse: TypeBourseFormGroupInput): void {
    const typeBourseRawValue = { ...this.getFormDefaults(), ...typeBourse };
    form.reset(
      {
        ...typeBourseRawValue,
        id: { value: typeBourseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeBourseFormDefaults {
    return {
      id: null,
    };
  }
}
