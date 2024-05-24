import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPays, NewPays } from '../pays.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPays for edit and NewPaysFormGroupInput for create.
 */
type PaysFormGroupInput = IPays | PartialWithRequiredKeyOf<NewPays>;

type PaysFormDefaults = Pick<NewPays, 'id' | 'zones'>;

type PaysFormGroupContent = {
  id: FormControl<IPays['id'] | NewPays['id']>;
  libellePays: FormControl<IPays['libellePays']>;
  paysEnAnglais: FormControl<IPays['paysEnAnglais']>;
  nationalite: FormControl<IPays['nationalite']>;
  codePays: FormControl<IPays['codePays']>;
  uEMOAYN: FormControl<IPays['uEMOAYN']>;
  cEDEAOYN: FormControl<IPays['cEDEAOYN']>;
  rIMYN: FormControl<IPays['rIMYN']>;
  autreYN: FormControl<IPays['autreYN']>;
  estEtrangerYN: FormControl<IPays['estEtrangerYN']>;
  zones: FormControl<IPays['zones']>;
};

export type PaysFormGroup = FormGroup<PaysFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaysFormService {
  createPaysFormGroup(pays: PaysFormGroupInput = { id: null }): PaysFormGroup {
    const paysRawValue = {
      ...this.getFormDefaults(),
      ...pays,
    };
    return new FormGroup<PaysFormGroupContent>({
      id: new FormControl(
        { value: paysRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libellePays: new FormControl(paysRawValue.libellePays, {
        validators: [Validators.required],
      }),
      paysEnAnglais: new FormControl(paysRawValue.paysEnAnglais),
      nationalite: new FormControl(paysRawValue.nationalite),
      codePays: new FormControl(paysRawValue.codePays),
      uEMOAYN: new FormControl(paysRawValue.uEMOAYN),
      cEDEAOYN: new FormControl(paysRawValue.cEDEAOYN),
      rIMYN: new FormControl(paysRawValue.rIMYN),
      autreYN: new FormControl(paysRawValue.autreYN),
      estEtrangerYN: new FormControl(paysRawValue.estEtrangerYN),
      zones: new FormControl(paysRawValue.zones ?? []),
    });
  }

  getPays(form: PaysFormGroup): IPays | NewPays {
    return form.getRawValue() as IPays | NewPays;
  }

  resetForm(form: PaysFormGroup, pays: PaysFormGroupInput): void {
    const paysRawValue = { ...this.getFormDefaults(), ...pays };
    form.reset(
      {
        ...paysRawValue,
        id: { value: paysRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaysFormDefaults {
    return {
      id: null,
      zones: [],
    };
  }
}
