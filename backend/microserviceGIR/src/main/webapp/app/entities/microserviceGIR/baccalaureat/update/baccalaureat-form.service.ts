import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBaccalaureat, NewBaccalaureat } from '../baccalaureat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBaccalaureat for edit and NewBaccalaureatFormGroupInput for create.
 */
type BaccalaureatFormGroupInput = IBaccalaureat | PartialWithRequiredKeyOf<NewBaccalaureat>;

type BaccalaureatFormDefaults = Pick<NewBaccalaureat, 'id' | 'actifYN'>;

type BaccalaureatFormGroupContent = {
  id: FormControl<IBaccalaureat['id'] | NewBaccalaureat['id']>;
  origineScolaire: FormControl<IBaccalaureat['origineScolaire']>;
  anneeBac: FormControl<IBaccalaureat['anneeBac']>;
  numeroTable: FormControl<IBaccalaureat['numeroTable']>;
  natureBac: FormControl<IBaccalaureat['natureBac']>;
  mentionBac: FormControl<IBaccalaureat['mentionBac']>;
  moyenneSelectionBac: FormControl<IBaccalaureat['moyenneSelectionBac']>;
  moyenneBac: FormControl<IBaccalaureat['moyenneBac']>;
  actifYN: FormControl<IBaccalaureat['actifYN']>;
  etudiant: FormControl<IBaccalaureat['etudiant']>;
  serie: FormControl<IBaccalaureat['serie']>;
};

export type BaccalaureatFormGroup = FormGroup<BaccalaureatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BaccalaureatFormService {
  createBaccalaureatFormGroup(baccalaureat: BaccalaureatFormGroupInput = { id: null }): BaccalaureatFormGroup {
    const baccalaureatRawValue = {
      ...this.getFormDefaults(),
      ...baccalaureat,
    };
    return new FormGroup<BaccalaureatFormGroupContent>({
      id: new FormControl(
        { value: baccalaureatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      origineScolaire: new FormControl(baccalaureatRawValue.origineScolaire),
      anneeBac: new FormControl(baccalaureatRawValue.anneeBac),
      numeroTable: new FormControl(baccalaureatRawValue.numeroTable),
      natureBac: new FormControl(baccalaureatRawValue.natureBac),
      mentionBac: new FormControl(baccalaureatRawValue.mentionBac),
      moyenneSelectionBac: new FormControl(baccalaureatRawValue.moyenneSelectionBac),
      moyenneBac: new FormControl(baccalaureatRawValue.moyenneBac),
      actifYN: new FormControl(baccalaureatRawValue.actifYN),
      etudiant: new FormControl(baccalaureatRawValue.etudiant),
      serie: new FormControl(baccalaureatRawValue.serie),
    });
  }

  getBaccalaureat(form: BaccalaureatFormGroup): IBaccalaureat | NewBaccalaureat {
    return form.getRawValue() as IBaccalaureat | NewBaccalaureat;
  }

  resetForm(form: BaccalaureatFormGroup, baccalaureat: BaccalaureatFormGroupInput): void {
    const baccalaureatRawValue = { ...this.getFormDefaults(), ...baccalaureat };
    form.reset(
      {
        ...baccalaureatRawValue,
        id: { value: baccalaureatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BaccalaureatFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
