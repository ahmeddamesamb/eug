import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeRapport, NewTypeRapport } from '../type-rapport.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeRapport for edit and NewTypeRapportFormGroupInput for create.
 */
type TypeRapportFormGroupInput = ITypeRapport | PartialWithRequiredKeyOf<NewTypeRapport>;

type TypeRapportFormDefaults = Pick<NewTypeRapport, 'id'>;

type TypeRapportFormGroupContent = {
  id: FormControl<ITypeRapport['id'] | NewTypeRapport['id']>;
  libelleTypeRapport: FormControl<ITypeRapport['libelleTypeRapport']>;
};

export type TypeRapportFormGroup = FormGroup<TypeRapportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeRapportFormService {
  createTypeRapportFormGroup(typeRapport: TypeRapportFormGroupInput = { id: null }): TypeRapportFormGroup {
    const typeRapportRawValue = {
      ...this.getFormDefaults(),
      ...typeRapport,
    };
    return new FormGroup<TypeRapportFormGroupContent>({
      id: new FormControl(
        { value: typeRapportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleTypeRapport: new FormControl(typeRapportRawValue.libelleTypeRapport),
    });
  }

  getTypeRapport(form: TypeRapportFormGroup): ITypeRapport | NewTypeRapport {
    return form.getRawValue() as ITypeRapport | NewTypeRapport;
  }

  resetForm(form: TypeRapportFormGroup, typeRapport: TypeRapportFormGroupInput): void {
    const typeRapportRawValue = { ...this.getFormDefaults(), ...typeRapport };
    form.reset(
      {
        ...typeRapportRawValue,
        id: { value: typeRapportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeRapportFormDefaults {
    return {
      id: null,
    };
  }
}
