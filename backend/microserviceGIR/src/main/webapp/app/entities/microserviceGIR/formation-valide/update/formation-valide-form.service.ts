import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFormationValide, NewFormationValide } from '../formation-valide.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormationValide for edit and NewFormationValideFormGroupInput for create.
 */
type FormationValideFormGroupInput = IFormationValide | PartialWithRequiredKeyOf<NewFormationValide>;

type FormationValideFormDefaults = Pick<NewFormationValide, 'id'>;

type FormationValideFormGroupContent = {
  id: FormControl<IFormationValide['id'] | NewFormationValide['id']>;
  valideYN: FormControl<IFormationValide['valideYN']>;
  dateDebut: FormControl<IFormationValide['dateDebut']>;
  dateFin: FormControl<IFormationValide['dateFin']>;
  formation: FormControl<IFormationValide['formation']>;
};

export type FormationValideFormGroup = FormGroup<FormationValideFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormationValideFormService {
  createFormationValideFormGroup(formationValide: FormationValideFormGroupInput = { id: null }): FormationValideFormGroup {
    const formationValideRawValue = {
      ...this.getFormDefaults(),
      ...formationValide,
    };
    return new FormGroup<FormationValideFormGroupContent>({
      id: new FormControl(
        { value: formationValideRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      valideYN: new FormControl(formationValideRawValue.valideYN),
      dateDebut: new FormControl(formationValideRawValue.dateDebut),
      dateFin: new FormControl(formationValideRawValue.dateFin),
      formation: new FormControl(formationValideRawValue.formation),
    });
  }

  getFormationValide(form: FormationValideFormGroup): IFormationValide | NewFormationValide {
    return form.getRawValue() as IFormationValide | NewFormationValide;
  }

  resetForm(form: FormationValideFormGroup, formationValide: FormationValideFormGroupInput): void {
    const formationValideRawValue = { ...this.getFormDefaults(), ...formationValide };
    form.reset(
      {
        ...formationValideRawValue,
        id: { value: formationValideRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FormationValideFormDefaults {
    return {
      id: null,
    };
  }
}
