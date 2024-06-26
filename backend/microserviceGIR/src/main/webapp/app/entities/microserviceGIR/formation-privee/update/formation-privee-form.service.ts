import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFormationPrivee, NewFormationPrivee } from '../formation-privee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormationPrivee for edit and NewFormationPriveeFormGroupInput for create.
 */
type FormationPriveeFormGroupInput = IFormationPrivee | PartialWithRequiredKeyOf<NewFormationPrivee>;

type FormationPriveeFormDefaults = Pick<
  NewFormationPrivee,
  'id' | 'paiementPremierMoisYN' | 'paiementDernierMoisYN' | 'fraisDossierYN' | 'actifYN'
>;

type FormationPriveeFormGroupContent = {
  id: FormControl<IFormationPrivee['id'] | NewFormationPrivee['id']>;
  nombreMensualites: FormControl<IFormationPrivee['nombreMensualites']>;
  paiementPremierMoisYN: FormControl<IFormationPrivee['paiementPremierMoisYN']>;
  paiementDernierMoisYN: FormControl<IFormationPrivee['paiementDernierMoisYN']>;
  fraisDossierYN: FormControl<IFormationPrivee['fraisDossierYN']>;
  coutTotal: FormControl<IFormationPrivee['coutTotal']>;
  mensualite: FormControl<IFormationPrivee['mensualite']>;
  actifYN: FormControl<IFormationPrivee['actifYN']>;
  formation: FormControl<IFormationPrivee['formation']>;
};

export type FormationPriveeFormGroup = FormGroup<FormationPriveeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormationPriveeFormService {
  createFormationPriveeFormGroup(formationPrivee: FormationPriveeFormGroupInput = { id: null }): FormationPriveeFormGroup {
    const formationPriveeRawValue = {
      ...this.getFormDefaults(),
      ...formationPrivee,
    };
    return new FormGroup<FormationPriveeFormGroupContent>({
      id: new FormControl(
        { value: formationPriveeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombreMensualites: new FormControl(formationPriveeRawValue.nombreMensualites, {
        validators: [Validators.required],
      }),
      paiementPremierMoisYN: new FormControl(formationPriveeRawValue.paiementPremierMoisYN),
      paiementDernierMoisYN: new FormControl(formationPriveeRawValue.paiementDernierMoisYN),
      fraisDossierYN: new FormControl(formationPriveeRawValue.fraisDossierYN),
      coutTotal: new FormControl(formationPriveeRawValue.coutTotal, {
        validators: [Validators.required],
      }),
      mensualite: new FormControl(formationPriveeRawValue.mensualite, {
        validators: [Validators.required],
      }),
      actifYN: new FormControl(formationPriveeRawValue.actifYN),
      formation: new FormControl(formationPriveeRawValue.formation),
    });
  }

  getFormationPrivee(form: FormationPriveeFormGroup): IFormationPrivee | NewFormationPrivee {
    return form.getRawValue() as IFormationPrivee | NewFormationPrivee;
  }

  resetForm(form: FormationPriveeFormGroup, formationPrivee: FormationPriveeFormGroupInput): void {
    const formationPriveeRawValue = { ...this.getFormDefaults(), ...formationPrivee };
    form.reset(
      {
        ...formationPriveeRawValue,
        id: { value: formationPriveeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FormationPriveeFormDefaults {
    return {
      id: null,
      paiementPremierMoisYN: false,
      paiementDernierMoisYN: false,
      fraisDossierYN: false,
      actifYN: false,
    };
  }
}
