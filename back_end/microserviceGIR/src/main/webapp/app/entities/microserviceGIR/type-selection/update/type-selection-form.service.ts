import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeSelection, NewTypeSelection } from '../type-selection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeSelection for edit and NewTypeSelectionFormGroupInput for create.
 */
type TypeSelectionFormGroupInput = ITypeSelection | PartialWithRequiredKeyOf<NewTypeSelection>;

type TypeSelectionFormDefaults = Pick<NewTypeSelection, 'id'>;

type TypeSelectionFormGroupContent = {
  id: FormControl<ITypeSelection['id'] | NewTypeSelection['id']>;
  libelleTypeSelection: FormControl<ITypeSelection['libelleTypeSelection']>;
};

export type TypeSelectionFormGroup = FormGroup<TypeSelectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeSelectionFormService {
  createTypeSelectionFormGroup(typeSelection: TypeSelectionFormGroupInput = { id: null }): TypeSelectionFormGroup {
    const typeSelectionRawValue = {
      ...this.getFormDefaults(),
      ...typeSelection,
    };
    return new FormGroup<TypeSelectionFormGroupContent>({
      id: new FormControl(
        { value: typeSelectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleTypeSelection: new FormControl(typeSelectionRawValue.libelleTypeSelection, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeSelection(form: TypeSelectionFormGroup): ITypeSelection | NewTypeSelection {
    return form.getRawValue() as ITypeSelection | NewTypeSelection;
  }

  resetForm(form: TypeSelectionFormGroup, typeSelection: TypeSelectionFormGroupInput): void {
    const typeSelectionRawValue = { ...this.getFormDefaults(), ...typeSelection };
    form.reset(
      {
        ...typeSelectionRawValue,
        id: { value: typeSelectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeSelectionFormDefaults {
    return {
      id: null,
    };
  }
}
