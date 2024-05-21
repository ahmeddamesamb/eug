import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEnseignement, NewEnseignement } from '../enseignement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnseignement for edit and NewEnseignementFormGroupInput for create.
 */
type EnseignementFormGroupInput = IEnseignement | PartialWithRequiredKeyOf<NewEnseignement>;

type EnseignementFormDefaults = Pick<NewEnseignement, 'id'>;

type EnseignementFormGroupContent = {
  id: FormControl<IEnseignement['id'] | NewEnseignement['id']>;
  libelleEnseignements: FormControl<IEnseignement['libelleEnseignements']>;
  volumeHoraire: FormControl<IEnseignement['volumeHoraire']>;
  nombreInscrits: FormControl<IEnseignement['nombreInscrits']>;
  groupeYN: FormControl<IEnseignement['groupeYN']>;
};

export type EnseignementFormGroup = FormGroup<EnseignementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnseignementFormService {
  createEnseignementFormGroup(enseignement: EnseignementFormGroupInput = { id: null }): EnseignementFormGroup {
    const enseignementRawValue = {
      ...this.getFormDefaults(),
      ...enseignement,
    };
    return new FormGroup<EnseignementFormGroupContent>({
      id: new FormControl(
        { value: enseignementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleEnseignements: new FormControl(enseignementRawValue.libelleEnseignements),
      volumeHoraire: new FormControl(enseignementRawValue.volumeHoraire),
      nombreInscrits: new FormControl(enseignementRawValue.nombreInscrits),
      groupeYN: new FormControl(enseignementRawValue.groupeYN),
    });
  }

  getEnseignement(form: EnseignementFormGroup): IEnseignement | NewEnseignement {
    return form.getRawValue() as IEnseignement | NewEnseignement;
  }

  resetForm(form: EnseignementFormGroup, enseignement: EnseignementFormGroupInput): void {
    const enseignementRawValue = { ...this.getFormDefaults(), ...enseignement };
    form.reset(
      {
        ...enseignementRawValue,
        id: { value: enseignementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnseignementFormDefaults {
    return {
      id: null,
    };
  }
}
