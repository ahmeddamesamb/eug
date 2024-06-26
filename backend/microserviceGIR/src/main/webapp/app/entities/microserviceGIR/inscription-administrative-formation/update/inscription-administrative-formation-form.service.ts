import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInscriptionAdministrativeFormation, NewInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInscriptionAdministrativeFormation for edit and NewInscriptionAdministrativeFormationFormGroupInput for create.
 */
type InscriptionAdministrativeFormationFormGroupInput =
  | IInscriptionAdministrativeFormation
  | PartialWithRequiredKeyOf<NewInscriptionAdministrativeFormation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInscriptionAdministrativeFormation | NewInscriptionAdministrativeFormation> = Omit<
  T,
  'dateChoixFormation' | 'dateValidationInscription'
> & {
  dateChoixFormation?: string | null;
  dateValidationInscription?: string | null;
};

type InscriptionAdministrativeFormationFormRawValue = FormValueOf<IInscriptionAdministrativeFormation>;

type NewInscriptionAdministrativeFormationFormRawValue = FormValueOf<NewInscriptionAdministrativeFormation>;

type InscriptionAdministrativeFormationFormDefaults = Pick<
  NewInscriptionAdministrativeFormation,
  | 'id'
  | 'inscriptionPrincipaleYN'
  | 'inscriptionAnnuleeYN'
  | 'exonereYN'
  | 'paiementFraisOblYN'
  | 'paiementFraisIntegergYN'
  | 'certificatDelivreYN'
  | 'dateChoixFormation'
  | 'dateValidationInscription'
>;

type InscriptionAdministrativeFormationFormGroupContent = {
  id: FormControl<InscriptionAdministrativeFormationFormRawValue['id'] | NewInscriptionAdministrativeFormation['id']>;
  inscriptionPrincipaleYN: FormControl<InscriptionAdministrativeFormationFormRawValue['inscriptionPrincipaleYN']>;
  inscriptionAnnuleeYN: FormControl<InscriptionAdministrativeFormationFormRawValue['inscriptionAnnuleeYN']>;
  exonereYN: FormControl<InscriptionAdministrativeFormationFormRawValue['exonereYN']>;
  paiementFraisOblYN: FormControl<InscriptionAdministrativeFormationFormRawValue['paiementFraisOblYN']>;
  paiementFraisIntegergYN: FormControl<InscriptionAdministrativeFormationFormRawValue['paiementFraisIntegergYN']>;
  certificatDelivreYN: FormControl<InscriptionAdministrativeFormationFormRawValue['certificatDelivreYN']>;
  dateChoixFormation: FormControl<InscriptionAdministrativeFormationFormRawValue['dateChoixFormation']>;
  dateValidationInscription: FormControl<InscriptionAdministrativeFormationFormRawValue['dateValidationInscription']>;
  inscriptionAdministrative: FormControl<InscriptionAdministrativeFormationFormRawValue['inscriptionAdministrative']>;
  formation: FormControl<InscriptionAdministrativeFormationFormRawValue['formation']>;
};

export type InscriptionAdministrativeFormationFormGroup = FormGroup<InscriptionAdministrativeFormationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InscriptionAdministrativeFormationFormService {
  createInscriptionAdministrativeFormationFormGroup(
    inscriptionAdministrativeFormation: InscriptionAdministrativeFormationFormGroupInput = { id: null },
  ): InscriptionAdministrativeFormationFormGroup {
    const inscriptionAdministrativeFormationRawValue =
      this.convertInscriptionAdministrativeFormationToInscriptionAdministrativeFormationRawValue({
        ...this.getFormDefaults(),
        ...inscriptionAdministrativeFormation,
      });
    return new FormGroup<InscriptionAdministrativeFormationFormGroupContent>({
      id: new FormControl(
        { value: inscriptionAdministrativeFormationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      inscriptionPrincipaleYN: new FormControl(inscriptionAdministrativeFormationRawValue.inscriptionPrincipaleYN),
      inscriptionAnnuleeYN: new FormControl(inscriptionAdministrativeFormationRawValue.inscriptionAnnuleeYN),
      exonereYN: new FormControl(inscriptionAdministrativeFormationRawValue.exonereYN, {
        validators: [Validators.required],
      }),
      paiementFraisOblYN: new FormControl(inscriptionAdministrativeFormationRawValue.paiementFraisOblYN),
      paiementFraisIntegergYN: new FormControl(inscriptionAdministrativeFormationRawValue.paiementFraisIntegergYN),
      certificatDelivreYN: new FormControl(inscriptionAdministrativeFormationRawValue.certificatDelivreYN),
      dateChoixFormation: new FormControl(inscriptionAdministrativeFormationRawValue.dateChoixFormation),
      dateValidationInscription: new FormControl(inscriptionAdministrativeFormationRawValue.dateValidationInscription),
      inscriptionAdministrative: new FormControl(inscriptionAdministrativeFormationRawValue.inscriptionAdministrative),
      formation: new FormControl(inscriptionAdministrativeFormationRawValue.formation),
    });
  }

  getInscriptionAdministrativeFormation(
    form: InscriptionAdministrativeFormationFormGroup,
  ): IInscriptionAdministrativeFormation | NewInscriptionAdministrativeFormation {
    return this.convertInscriptionAdministrativeFormationRawValueToInscriptionAdministrativeFormation(
      form.getRawValue() as InscriptionAdministrativeFormationFormRawValue | NewInscriptionAdministrativeFormationFormRawValue,
    );
  }

  resetForm(
    form: InscriptionAdministrativeFormationFormGroup,
    inscriptionAdministrativeFormation: InscriptionAdministrativeFormationFormGroupInput,
  ): void {
    const inscriptionAdministrativeFormationRawValue =
      this.convertInscriptionAdministrativeFormationToInscriptionAdministrativeFormationRawValue({
        ...this.getFormDefaults(),
        ...inscriptionAdministrativeFormation,
      });
    form.reset(
      {
        ...inscriptionAdministrativeFormationRawValue,
        id: { value: inscriptionAdministrativeFormationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InscriptionAdministrativeFormationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inscriptionPrincipaleYN: false,
      inscriptionAnnuleeYN: false,
      exonereYN: false,
      paiementFraisOblYN: false,
      paiementFraisIntegergYN: false,
      certificatDelivreYN: false,
      dateChoixFormation: currentTime,
      dateValidationInscription: currentTime,
    };
  }

  private convertInscriptionAdministrativeFormationRawValueToInscriptionAdministrativeFormation(
    rawInscriptionAdministrativeFormation:
      | InscriptionAdministrativeFormationFormRawValue
      | NewInscriptionAdministrativeFormationFormRawValue,
  ): IInscriptionAdministrativeFormation | NewInscriptionAdministrativeFormation {
    return {
      ...rawInscriptionAdministrativeFormation,
      dateChoixFormation: dayjs(rawInscriptionAdministrativeFormation.dateChoixFormation, DATE_TIME_FORMAT),
      dateValidationInscription: dayjs(rawInscriptionAdministrativeFormation.dateValidationInscription, DATE_TIME_FORMAT),
    };
  }

  private convertInscriptionAdministrativeFormationToInscriptionAdministrativeFormationRawValue(
    inscriptionAdministrativeFormation:
      | IInscriptionAdministrativeFormation
      | (Partial<NewInscriptionAdministrativeFormation> & InscriptionAdministrativeFormationFormDefaults),
  ): InscriptionAdministrativeFormationFormRawValue | PartialWithRequiredKeyOf<NewInscriptionAdministrativeFormationFormRawValue> {
    return {
      ...inscriptionAdministrativeFormation,
      dateChoixFormation: inscriptionAdministrativeFormation.dateChoixFormation
        ? inscriptionAdministrativeFormation.dateChoixFormation.format(DATE_TIME_FORMAT)
        : undefined,
      dateValidationInscription: inscriptionAdministrativeFormation.dateValidationInscription
        ? inscriptionAdministrativeFormation.dateValidationInscription.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
