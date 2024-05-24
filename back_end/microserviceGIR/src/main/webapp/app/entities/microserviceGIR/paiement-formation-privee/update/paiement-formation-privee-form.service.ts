import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPaiementFormationPrivee, NewPaiementFormationPrivee } from '../paiement-formation-privee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiementFormationPrivee for edit and NewPaiementFormationPriveeFormGroupInput for create.
 */
type PaiementFormationPriveeFormGroupInput = IPaiementFormationPrivee | PartialWithRequiredKeyOf<NewPaiementFormationPrivee>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPaiementFormationPrivee | NewPaiementFormationPrivee> = Omit<T, 'datePaiement'> & {
  datePaiement?: string | null;
};

type PaiementFormationPriveeFormRawValue = FormValueOf<IPaiementFormationPrivee>;

type NewPaiementFormationPriveeFormRawValue = FormValueOf<NewPaiementFormationPrivee>;

type PaiementFormationPriveeFormDefaults = Pick<NewPaiementFormationPrivee, 'id' | 'datePaiement'>;

type PaiementFormationPriveeFormGroupContent = {
  id: FormControl<PaiementFormationPriveeFormRawValue['id'] | NewPaiementFormationPrivee['id']>;
  datePaiement: FormControl<PaiementFormationPriveeFormRawValue['datePaiement']>;
  moisPaiement: FormControl<PaiementFormationPriveeFormRawValue['moisPaiement']>;
  anneePaiement: FormControl<PaiementFormationPriveeFormRawValue['anneePaiement']>;
  payerMensualiteYN: FormControl<PaiementFormationPriveeFormRawValue['payerMensualiteYN']>;
  payerDelaisYN: FormControl<PaiementFormationPriveeFormRawValue['payerDelaisYN']>;
  emailUser: FormControl<PaiementFormationPriveeFormRawValue['emailUser']>;
  inscriptionAdministrativeFormation: FormControl<PaiementFormationPriveeFormRawValue['inscriptionAdministrativeFormation']>;
  operateur: FormControl<PaiementFormationPriveeFormRawValue['operateur']>;
};

export type PaiementFormationPriveeFormGroup = FormGroup<PaiementFormationPriveeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementFormationPriveeFormService {
  createPaiementFormationPriveeFormGroup(
    paiementFormationPrivee: PaiementFormationPriveeFormGroupInput = { id: null },
  ): PaiementFormationPriveeFormGroup {
    const paiementFormationPriveeRawValue = this.convertPaiementFormationPriveeToPaiementFormationPriveeRawValue({
      ...this.getFormDefaults(),
      ...paiementFormationPrivee,
    });
    return new FormGroup<PaiementFormationPriveeFormGroupContent>({
      id: new FormControl(
        { value: paiementFormationPriveeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      datePaiement: new FormControl(paiementFormationPriveeRawValue.datePaiement),
      moisPaiement: new FormControl(paiementFormationPriveeRawValue.moisPaiement),
      anneePaiement: new FormControl(paiementFormationPriveeRawValue.anneePaiement),
      payerMensualiteYN: new FormControl(paiementFormationPriveeRawValue.payerMensualiteYN),
      payerDelaisYN: new FormControl(paiementFormationPriveeRawValue.payerDelaisYN),
      emailUser: new FormControl(paiementFormationPriveeRawValue.emailUser),
      inscriptionAdministrativeFormation: new FormControl(paiementFormationPriveeRawValue.inscriptionAdministrativeFormation),
      operateur: new FormControl(paiementFormationPriveeRawValue.operateur),
    });
  }

  getPaiementFormationPrivee(form: PaiementFormationPriveeFormGroup): IPaiementFormationPrivee | NewPaiementFormationPrivee {
    return this.convertPaiementFormationPriveeRawValueToPaiementFormationPrivee(
      form.getRawValue() as PaiementFormationPriveeFormRawValue | NewPaiementFormationPriveeFormRawValue,
    );
  }

  resetForm(form: PaiementFormationPriveeFormGroup, paiementFormationPrivee: PaiementFormationPriveeFormGroupInput): void {
    const paiementFormationPriveeRawValue = this.convertPaiementFormationPriveeToPaiementFormationPriveeRawValue({
      ...this.getFormDefaults(),
      ...paiementFormationPrivee,
    });
    form.reset(
      {
        ...paiementFormationPriveeRawValue,
        id: { value: paiementFormationPriveeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaiementFormationPriveeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      datePaiement: currentTime,
    };
  }

  private convertPaiementFormationPriveeRawValueToPaiementFormationPrivee(
    rawPaiementFormationPrivee: PaiementFormationPriveeFormRawValue | NewPaiementFormationPriveeFormRawValue,
  ): IPaiementFormationPrivee | NewPaiementFormationPrivee {
    return {
      ...rawPaiementFormationPrivee,
      datePaiement: dayjs(rawPaiementFormationPrivee.datePaiement, DATE_TIME_FORMAT),
    };
  }

  private convertPaiementFormationPriveeToPaiementFormationPriveeRawValue(
    paiementFormationPrivee: IPaiementFormationPrivee | (Partial<NewPaiementFormationPrivee> & PaiementFormationPriveeFormDefaults),
  ): PaiementFormationPriveeFormRawValue | PartialWithRequiredKeyOf<NewPaiementFormationPriveeFormRawValue> {
    return {
      ...paiementFormationPrivee,
      datePaiement: paiementFormationPrivee.datePaiement ? paiementFormationPrivee.datePaiement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
