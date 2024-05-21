import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INiveau, NewNiveau } from '../niveau.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INiveau for edit and NewNiveauFormGroupInput for create.
 */
type NiveauFormGroupInput = INiveau | PartialWithRequiredKeyOf<NewNiveau>;

type NiveauFormDefaults = Pick<NewNiveau, 'id'>;

type NiveauFormGroupContent = {
  id: FormControl<INiveau['id'] | NewNiveau['id']>;
  libelleNiveau: FormControl<INiveau['libelleNiveau']>;
  cycleNiveau: FormControl<INiveau['cycleNiveau']>;
  codeNiveau: FormControl<INiveau['codeNiveau']>;
  anneeEtude: FormControl<INiveau['anneeEtude']>;
};

export type NiveauFormGroup = FormGroup<NiveauFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NiveauFormService {
  createNiveauFormGroup(niveau: NiveauFormGroupInput = { id: null }): NiveauFormGroup {
    const niveauRawValue = {
      ...this.getFormDefaults(),
      ...niveau,
    };
    return new FormGroup<NiveauFormGroupContent>({
      id: new FormControl(
        { value: niveauRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleNiveau: new FormControl(niveauRawValue.libelleNiveau),
      cycleNiveau: new FormControl(niveauRawValue.cycleNiveau),
      codeNiveau: new FormControl(niveauRawValue.codeNiveau),
      anneeEtude: new FormControl(niveauRawValue.anneeEtude),
    });
  }

  getNiveau(form: NiveauFormGroup): INiveau | NewNiveau {
    return form.getRawValue() as INiveau | NewNiveau;
  }

  resetForm(form: NiveauFormGroup, niveau: NiveauFormGroupInput): void {
    const niveauRawValue = { ...this.getFormDefaults(), ...niveau };
    form.reset(
      {
        ...niveauRawValue,
        id: { value: niveauRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NiveauFormDefaults {
    return {
      id: null,
    };
  }
}
