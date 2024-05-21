import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFormation, NewFormation } from '../formation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormation for edit and NewFormationFormGroupInput for create.
 */
type FormationFormGroupInput = IFormation | PartialWithRequiredKeyOf<NewFormation>;

type FormationFormDefaults = Pick<NewFormation, 'id'>;

type FormationFormGroupContent = {
  id: FormControl<IFormation['id'] | NewFormation['id']>;
  nombreMensualites: FormControl<IFormation['nombreMensualites']>;
  fraisDossierYN: FormControl<IFormation['fraisDossierYN']>;
  classeDiplomanteYN: FormControl<IFormation['classeDiplomanteYN']>;
  libelleDiplome: FormControl<IFormation['libelleDiplome']>;
  codeFormation: FormControl<IFormation['codeFormation']>;
  nbreCreditsMin: FormControl<IFormation['nbreCreditsMin']>;
  estParcoursYN: FormControl<IFormation['estParcoursYN']>;
  lmdYN: FormControl<IFormation['lmdYN']>;
  specialite: FormControl<IFormation['specialite']>;
  niveau: FormControl<IFormation['niveau']>;
};

export type FormationFormGroup = FormGroup<FormationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormationFormService {
  createFormationFormGroup(formation: FormationFormGroupInput = { id: null }): FormationFormGroup {
    const formationRawValue = {
      ...this.getFormDefaults(),
      ...formation,
    };
    return new FormGroup<FormationFormGroupContent>({
      id: new FormControl(
        { value: formationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombreMensualites: new FormControl(formationRawValue.nombreMensualites),
      fraisDossierYN: new FormControl(formationRawValue.fraisDossierYN),
      classeDiplomanteYN: new FormControl(formationRawValue.classeDiplomanteYN),
      libelleDiplome: new FormControl(formationRawValue.libelleDiplome),
      codeFormation: new FormControl(formationRawValue.codeFormation),
      nbreCreditsMin: new FormControl(formationRawValue.nbreCreditsMin),
      estParcoursYN: new FormControl(formationRawValue.estParcoursYN),
      lmdYN: new FormControl(formationRawValue.lmdYN),
      specialite: new FormControl(formationRawValue.specialite),
      niveau: new FormControl(formationRawValue.niveau),
    });
  }

  getFormation(form: FormationFormGroup): IFormation | NewFormation {
    return form.getRawValue() as IFormation | NewFormation;
  }

  resetForm(form: FormationFormGroup, formation: FormationFormGroupInput): void {
    const formationRawValue = { ...this.getFormDefaults(), ...formation };
    form.reset(
      {
        ...formationRawValue,
        id: { value: formationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FormationFormDefaults {
    return {
      id: null,
    };
  }
}
