import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaiementFrais, NewPaiementFrais } from '../paiement-frais.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiementFrais for edit and NewPaiementFraisFormGroupInput for create.
 */
type PaiementFraisFormGroupInput = IPaiementFrais | PartialWithRequiredKeyOf<NewPaiementFrais>;

type PaiementFraisFormDefaults = Pick<NewPaiementFrais, 'id' | 'obligatoireYN' | 'echeancePayeeYN' | 'forclosYN' | 'paimentDelaiYN'>;

type PaiementFraisFormGroupContent = {
  id: FormControl<IPaiementFrais['id'] | NewPaiementFrais['id']>;
  datePaiement: FormControl<IPaiementFrais['datePaiement']>;
  obligatoireYN: FormControl<IPaiementFrais['obligatoireYN']>;
  echeancePayeeYN: FormControl<IPaiementFrais['echeancePayeeYN']>;
  emailUser: FormControl<IPaiementFrais['emailUser']>;
  dateForclos: FormControl<IPaiementFrais['dateForclos']>;
  forclosYN: FormControl<IPaiementFrais['forclosYN']>;
  paimentDelaiYN: FormControl<IPaiementFrais['paimentDelaiYN']>;
  frais: FormControl<IPaiementFrais['frais']>;
  operateur: FormControl<IPaiementFrais['operateur']>;
  inscriptionAdministrativeFormation: FormControl<IPaiementFrais['inscriptionAdministrativeFormation']>;
};

export type PaiementFraisFormGroup = FormGroup<PaiementFraisFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementFraisFormService {
  createPaiementFraisFormGroup(paiementFrais: PaiementFraisFormGroupInput = { id: null }): PaiementFraisFormGroup {
    const paiementFraisRawValue = {
      ...this.getFormDefaults(),
      ...paiementFrais,
    };
    return new FormGroup<PaiementFraisFormGroupContent>({
      id: new FormControl(
        { value: paiementFraisRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      datePaiement: new FormControl(paiementFraisRawValue.datePaiement, {
        validators: [Validators.required],
      }),
      obligatoireYN: new FormControl(paiementFraisRawValue.obligatoireYN),
      echeancePayeeYN: new FormControl(paiementFraisRawValue.echeancePayeeYN),
      emailUser: new FormControl(paiementFraisRawValue.emailUser),
      dateForclos: new FormControl(paiementFraisRawValue.dateForclos),
      forclosYN: new FormControl(paiementFraisRawValue.forclosYN),
      paimentDelaiYN: new FormControl(paiementFraisRawValue.paimentDelaiYN),
      frais: new FormControl(paiementFraisRawValue.frais),
      operateur: new FormControl(paiementFraisRawValue.operateur),
      inscriptionAdministrativeFormation: new FormControl(paiementFraisRawValue.inscriptionAdministrativeFormation),
    });
  }

  getPaiementFrais(form: PaiementFraisFormGroup): IPaiementFrais | NewPaiementFrais {
    return form.getRawValue() as IPaiementFrais | NewPaiementFrais;
  }

  resetForm(form: PaiementFraisFormGroup, paiementFrais: PaiementFraisFormGroupInput): void {
    const paiementFraisRawValue = { ...this.getFormDefaults(), ...paiementFrais };
    form.reset(
      {
        ...paiementFraisRawValue,
        id: { value: paiementFraisRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaiementFraisFormDefaults {
    return {
      id: null,
      obligatoireYN: false,
      echeancePayeeYN: false,
      forclosYN: false,
      paimentDelaiYN: false,
    };
  }
}
