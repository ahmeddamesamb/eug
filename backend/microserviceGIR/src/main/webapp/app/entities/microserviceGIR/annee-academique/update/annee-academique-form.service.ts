import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAnneeAcademique, NewAnneeAcademique } from '../annee-academique.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAnneeAcademique for edit and NewAnneeAcademiqueFormGroupInput for create.
 */
type AnneeAcademiqueFormGroupInput = IAnneeAcademique | PartialWithRequiredKeyOf<NewAnneeAcademique>;

type AnneeAcademiqueFormDefaults = Pick<NewAnneeAcademique, 'id' | 'anneeCouranteYN'>;

type AnneeAcademiqueFormGroupContent = {
  id: FormControl<IAnneeAcademique['id'] | NewAnneeAcademique['id']>;
  libelleAnneeAcademique: FormControl<IAnneeAcademique['libelleAnneeAcademique']>;
  anneeAc: FormControl<IAnneeAcademique['anneeAc']>;
  separateur: FormControl<IAnneeAcademique['separateur']>;
  anneeCouranteYN: FormControl<IAnneeAcademique['anneeCouranteYN']>;
};

export type AnneeAcademiqueFormGroup = FormGroup<AnneeAcademiqueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AnneeAcademiqueFormService {
  createAnneeAcademiqueFormGroup(anneeAcademique: AnneeAcademiqueFormGroupInput = { id: null }): AnneeAcademiqueFormGroup {
    const anneeAcademiqueRawValue = {
      ...this.getFormDefaults(),
      ...anneeAcademique,
    };
    return new FormGroup<AnneeAcademiqueFormGroupContent>({
      id: new FormControl(
        { value: anneeAcademiqueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleAnneeAcademique: new FormControl(anneeAcademiqueRawValue.libelleAnneeAcademique, {
        validators: [Validators.required],
      }),
      anneeAc: new FormControl(anneeAcademiqueRawValue.anneeAc, {
        validators: [Validators.required, Validators.min(1990), Validators.max(2060)],
      }),
      separateur: new FormControl(anneeAcademiqueRawValue.separateur, {
        validators: [Validators.required, Validators.maxLength(1)],
      }),
      anneeCouranteYN: new FormControl(anneeAcademiqueRawValue.anneeCouranteYN),
    });
  }

  getAnneeAcademique(form: AnneeAcademiqueFormGroup): IAnneeAcademique | NewAnneeAcademique {
    return form.getRawValue() as IAnneeAcademique | NewAnneeAcademique;
  }

  resetForm(form: AnneeAcademiqueFormGroup, anneeAcademique: AnneeAcademiqueFormGroupInput): void {
    const anneeAcademiqueRawValue = { ...this.getFormDefaults(), ...anneeAcademique };
    form.reset(
      {
        ...anneeAcademiqueRawValue,
        id: { value: anneeAcademiqueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AnneeAcademiqueFormDefaults {
    return {
      id: null,
      anneeCouranteYN: false,
    };
  }
}
