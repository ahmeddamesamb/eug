import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeFrais, NewTypeFrais } from '../type-frais.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeFrais for edit and NewTypeFraisFormGroupInput for create.
 */
type TypeFraisFormGroupInput = ITypeFrais | PartialWithRequiredKeyOf<NewTypeFrais>;

type TypeFraisFormDefaults = Pick<NewTypeFrais, 'id'>;

type TypeFraisFormGroupContent = {
  id: FormControl<ITypeFrais['id'] | NewTypeFrais['id']>;
  libelle: FormControl<ITypeFrais['libelle']>;
};

export type TypeFraisFormGroup = FormGroup<TypeFraisFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeFraisFormService {
  createTypeFraisFormGroup(typeFrais: TypeFraisFormGroupInput = { id: null }): TypeFraisFormGroup {
    const typeFraisRawValue = {
      ...this.getFormDefaults(),
      ...typeFrais,
    };
    return new FormGroup<TypeFraisFormGroupContent>({
      id: new FormControl(
        { value: typeFraisRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelle: new FormControl(typeFraisRawValue.libelle),
    });
  }

  getTypeFrais(form: TypeFraisFormGroup): ITypeFrais | NewTypeFrais {
    return form.getRawValue() as ITypeFrais | NewTypeFrais;
  }

  resetForm(form: TypeFraisFormGroup, typeFrais: TypeFraisFormGroupInput): void {
    const typeFraisRawValue = { ...this.getFormDefaults(), ...typeFrais };
    form.reset(
      {
        ...typeFraisRawValue,
        id: { value: typeFraisRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeFraisFormDefaults {
    return {
      id: null,
    };
  }
}
