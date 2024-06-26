import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFormationInvalide, NewFormationInvalide } from '../formation-invalide.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormationInvalide for edit and NewFormationInvalideFormGroupInput for create.
 */
type FormationInvalideFormGroupInput = IFormationInvalide | PartialWithRequiredKeyOf<NewFormationInvalide>;

type FormationInvalideFormDefaults = Pick<NewFormationInvalide, 'id' | 'actifYN'>;

type FormationInvalideFormGroupContent = {
  id: FormControl<IFormationInvalide['id'] | NewFormationInvalide['id']>;
  actifYN: FormControl<IFormationInvalide['actifYN']>;
  formation: FormControl<IFormationInvalide['formation']>;
  anneeAcademique: FormControl<IFormationInvalide['anneeAcademique']>;
};

export type FormationInvalideFormGroup = FormGroup<FormationInvalideFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormationInvalideFormService {
  createFormationInvalideFormGroup(formationInvalide: FormationInvalideFormGroupInput = { id: null }): FormationInvalideFormGroup {
    const formationInvalideRawValue = {
      ...this.getFormDefaults(),
      ...formationInvalide,
    };
    return new FormGroup<FormationInvalideFormGroupContent>({
      id: new FormControl(
        { value: formationInvalideRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      actifYN: new FormControl(formationInvalideRawValue.actifYN),
      formation: new FormControl(formationInvalideRawValue.formation),
      anneeAcademique: new FormControl(formationInvalideRawValue.anneeAcademique),
    });
  }

  getFormationInvalide(form: FormationInvalideFormGroup): IFormationInvalide | NewFormationInvalide {
    return form.getRawValue() as IFormationInvalide | NewFormationInvalide;
  }

  resetForm(form: FormationInvalideFormGroup, formationInvalide: FormationInvalideFormGroupInput): void {
    const formationInvalideRawValue = { ...this.getFormDefaults(), ...formationInvalide };
    form.reset(
      {
        ...formationInvalideRawValue,
        id: { value: formationInvalideRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FormationInvalideFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
