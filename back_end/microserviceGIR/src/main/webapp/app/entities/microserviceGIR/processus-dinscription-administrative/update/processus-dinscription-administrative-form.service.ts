import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import {
  IProcessusDinscriptionAdministrative,
  NewProcessusDinscriptionAdministrative,
} from '../processus-dinscription-administrative.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcessusDinscriptionAdministrative for edit and NewProcessusDinscriptionAdministrativeFormGroupInput for create.
 */
type ProcessusDinscriptionAdministrativeFormGroupInput =
  | IProcessusDinscriptionAdministrative
  | PartialWithRequiredKeyOf<NewProcessusDinscriptionAdministrative>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProcessusDinscriptionAdministrative | NewProcessusDinscriptionAdministrative> = Omit<
  T,
  | 'dateDemarrageInscription'
  | 'dateAnnulationInscription'
  | 'dateVisiteMedicale'
  | 'dateRemiseQuitusCROUS'
  | 'dateRemiseQuitusBU'
  | 'datePaiementFraisObligatoires'
  | 'dateRemiseCarteEtu'
  | 'dateInscriptionAdministrative'
  | 'derniereModification'
> & {
  dateDemarrageInscription?: string | null;
  dateAnnulationInscription?: string | null;
  dateVisiteMedicale?: string | null;
  dateRemiseQuitusCROUS?: string | null;
  dateRemiseQuitusBU?: string | null;
  datePaiementFraisObligatoires?: string | null;
  dateRemiseCarteEtu?: string | null;
  dateInscriptionAdministrative?: string | null;
  derniereModification?: string | null;
};

type ProcessusDinscriptionAdministrativeFormRawValue = FormValueOf<IProcessusDinscriptionAdministrative>;

type NewProcessusDinscriptionAdministrativeFormRawValue = FormValueOf<NewProcessusDinscriptionAdministrative>;

type ProcessusDinscriptionAdministrativeFormDefaults = Pick<
  NewProcessusDinscriptionAdministrative,
  | 'id'
  | 'dateDemarrageInscription'
  | 'dateAnnulationInscription'
  | 'dateVisiteMedicale'
  | 'dateRemiseQuitusCROUS'
  | 'dateRemiseQuitusBU'
  | 'datePaiementFraisObligatoires'
  | 'dateRemiseCarteEtu'
  | 'dateInscriptionAdministrative'
  | 'derniereModification'
>;

type ProcessusDinscriptionAdministrativeFormGroupContent = {
  id: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['id'] | NewProcessusDinscriptionAdministrative['id']>;
  inscriptionDemarreeYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['inscriptionDemarreeYN']>;
  dateDemarrageInscription: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateDemarrageInscription']>;
  inscriptionAnnuleeYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['inscriptionAnnuleeYN']>;
  dateAnnulationInscription: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateAnnulationInscription']>;
  apteYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['apteYN']>;
  dateVisiteMedicale: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateVisiteMedicale']>;
  beneficiaireCROUSYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['beneficiaireCROUSYN']>;
  dateRemiseQuitusCROUS: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateRemiseQuitusCROUS']>;
  nouveauCodeBUYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['nouveauCodeBUYN']>;
  quitusBUYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['quitusBUYN']>;
  dateRemiseQuitusBU: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateRemiseQuitusBU']>;
  exporterBUYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['exporterBUYN']>;
  fraisObligatoiresPayesYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['fraisObligatoiresPayesYN']>;
  datePaiementFraisObligatoires: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['datePaiementFraisObligatoires']>;
  numeroQuitusCROUS: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['numeroQuitusCROUS']>;
  carteEturemiseYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['carteEturemiseYN']>;
  carteDupremiseYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['carteDupremiseYN']>;
  dateRemiseCarteEtu: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateRemiseCarteEtu']>;
  dateRemiseCarteDup: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateRemiseCarteDup']>;
  inscritAdministrativementYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['inscritAdministrativementYN']>;
  dateInscriptionAdministrative: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['dateInscriptionAdministrative']>;
  derniereModification: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['derniereModification']>;
  inscritOnlineYN: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['inscritOnlineYN']>;
  emailUser: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['emailUser']>;
  inscriptionAdministrative: FormControl<ProcessusDinscriptionAdministrativeFormRawValue['inscriptionAdministrative']>;
};

export type ProcessusDinscriptionAdministrativeFormGroup = FormGroup<ProcessusDinscriptionAdministrativeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcessusDinscriptionAdministrativeFormService {
  createProcessusDinscriptionAdministrativeFormGroup(
    processusDinscriptionAdministrative: ProcessusDinscriptionAdministrativeFormGroupInput = { id: null },
  ): ProcessusDinscriptionAdministrativeFormGroup {
    const processusDinscriptionAdministrativeRawValue =
      this.convertProcessusDinscriptionAdministrativeToProcessusDinscriptionAdministrativeRawValue({
        ...this.getFormDefaults(),
        ...processusDinscriptionAdministrative,
      });
    return new FormGroup<ProcessusDinscriptionAdministrativeFormGroupContent>({
      id: new FormControl(
        { value: processusDinscriptionAdministrativeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      inscriptionDemarreeYN: new FormControl(processusDinscriptionAdministrativeRawValue.inscriptionDemarreeYN),
      dateDemarrageInscription: new FormControl(processusDinscriptionAdministrativeRawValue.dateDemarrageInscription),
      inscriptionAnnuleeYN: new FormControl(processusDinscriptionAdministrativeRawValue.inscriptionAnnuleeYN),
      dateAnnulationInscription: new FormControl(processusDinscriptionAdministrativeRawValue.dateAnnulationInscription),
      apteYN: new FormControl(processusDinscriptionAdministrativeRawValue.apteYN),
      dateVisiteMedicale: new FormControl(processusDinscriptionAdministrativeRawValue.dateVisiteMedicale),
      beneficiaireCROUSYN: new FormControl(processusDinscriptionAdministrativeRawValue.beneficiaireCROUSYN),
      dateRemiseQuitusCROUS: new FormControl(processusDinscriptionAdministrativeRawValue.dateRemiseQuitusCROUS),
      nouveauCodeBUYN: new FormControl(processusDinscriptionAdministrativeRawValue.nouveauCodeBUYN),
      quitusBUYN: new FormControl(processusDinscriptionAdministrativeRawValue.quitusBUYN),
      dateRemiseQuitusBU: new FormControl(processusDinscriptionAdministrativeRawValue.dateRemiseQuitusBU),
      exporterBUYN: new FormControl(processusDinscriptionAdministrativeRawValue.exporterBUYN),
      fraisObligatoiresPayesYN: new FormControl(processusDinscriptionAdministrativeRawValue.fraisObligatoiresPayesYN),
      datePaiementFraisObligatoires: new FormControl(processusDinscriptionAdministrativeRawValue.datePaiementFraisObligatoires),
      numeroQuitusCROUS: new FormControl(processusDinscriptionAdministrativeRawValue.numeroQuitusCROUS),
      carteEturemiseYN: new FormControl(processusDinscriptionAdministrativeRawValue.carteEturemiseYN),
      carteDupremiseYN: new FormControl(processusDinscriptionAdministrativeRawValue.carteDupremiseYN),
      dateRemiseCarteEtu: new FormControl(processusDinscriptionAdministrativeRawValue.dateRemiseCarteEtu),
      dateRemiseCarteDup: new FormControl(processusDinscriptionAdministrativeRawValue.dateRemiseCarteDup),
      inscritAdministrativementYN: new FormControl(processusDinscriptionAdministrativeRawValue.inscritAdministrativementYN),
      dateInscriptionAdministrative: new FormControl(processusDinscriptionAdministrativeRawValue.dateInscriptionAdministrative),
      derniereModification: new FormControl(processusDinscriptionAdministrativeRawValue.derniereModification),
      inscritOnlineYN: new FormControl(processusDinscriptionAdministrativeRawValue.inscritOnlineYN),
      emailUser: new FormControl(processusDinscriptionAdministrativeRawValue.emailUser),
      inscriptionAdministrative: new FormControl(processusDinscriptionAdministrativeRawValue.inscriptionAdministrative),
    });
  }

  getProcessusDinscriptionAdministrative(
    form: ProcessusDinscriptionAdministrativeFormGroup,
  ): IProcessusDinscriptionAdministrative | NewProcessusDinscriptionAdministrative {
    return this.convertProcessusDinscriptionAdministrativeRawValueToProcessusDinscriptionAdministrative(
      form.getRawValue() as ProcessusDinscriptionAdministrativeFormRawValue | NewProcessusDinscriptionAdministrativeFormRawValue,
    );
  }

  resetForm(
    form: ProcessusDinscriptionAdministrativeFormGroup,
    processusDinscriptionAdministrative: ProcessusDinscriptionAdministrativeFormGroupInput,
  ): void {
    const processusDinscriptionAdministrativeRawValue =
      this.convertProcessusDinscriptionAdministrativeToProcessusDinscriptionAdministrativeRawValue({
        ...this.getFormDefaults(),
        ...processusDinscriptionAdministrative,
      });
    form.reset(
      {
        ...processusDinscriptionAdministrativeRawValue,
        id: { value: processusDinscriptionAdministrativeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProcessusDinscriptionAdministrativeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDemarrageInscription: currentTime,
      dateAnnulationInscription: currentTime,
      dateVisiteMedicale: currentTime,
      dateRemiseQuitusCROUS: currentTime,
      dateRemiseQuitusBU: currentTime,
      datePaiementFraisObligatoires: currentTime,
      dateRemiseCarteEtu: currentTime,
      dateInscriptionAdministrative: currentTime,
      derniereModification: currentTime,
    };
  }

  private convertProcessusDinscriptionAdministrativeRawValueToProcessusDinscriptionAdministrative(
    rawProcessusDinscriptionAdministrative:
      | ProcessusDinscriptionAdministrativeFormRawValue
      | NewProcessusDinscriptionAdministrativeFormRawValue,
  ): IProcessusDinscriptionAdministrative | NewProcessusDinscriptionAdministrative {
    return {
      ...rawProcessusDinscriptionAdministrative,
      dateDemarrageInscription: dayjs(rawProcessusDinscriptionAdministrative.dateDemarrageInscription, DATE_TIME_FORMAT),
      dateAnnulationInscription: dayjs(rawProcessusDinscriptionAdministrative.dateAnnulationInscription, DATE_TIME_FORMAT),
      dateVisiteMedicale: dayjs(rawProcessusDinscriptionAdministrative.dateVisiteMedicale, DATE_TIME_FORMAT),
      dateRemiseQuitusCROUS: dayjs(rawProcessusDinscriptionAdministrative.dateRemiseQuitusCROUS, DATE_TIME_FORMAT),
      dateRemiseQuitusBU: dayjs(rawProcessusDinscriptionAdministrative.dateRemiseQuitusBU, DATE_TIME_FORMAT),
      datePaiementFraisObligatoires: dayjs(rawProcessusDinscriptionAdministrative.datePaiementFraisObligatoires, DATE_TIME_FORMAT),
      dateRemiseCarteEtu: dayjs(rawProcessusDinscriptionAdministrative.dateRemiseCarteEtu, DATE_TIME_FORMAT),
      dateInscriptionAdministrative: dayjs(rawProcessusDinscriptionAdministrative.dateInscriptionAdministrative, DATE_TIME_FORMAT),
      derniereModification: dayjs(rawProcessusDinscriptionAdministrative.derniereModification, DATE_TIME_FORMAT),
    };
  }

  private convertProcessusDinscriptionAdministrativeToProcessusDinscriptionAdministrativeRawValue(
    processusDinscriptionAdministrative:
      | IProcessusDinscriptionAdministrative
      | (Partial<NewProcessusDinscriptionAdministrative> & ProcessusDinscriptionAdministrativeFormDefaults),
  ): ProcessusDinscriptionAdministrativeFormRawValue | PartialWithRequiredKeyOf<NewProcessusDinscriptionAdministrativeFormRawValue> {
    return {
      ...processusDinscriptionAdministrative,
      dateDemarrageInscription: processusDinscriptionAdministrative.dateDemarrageInscription
        ? processusDinscriptionAdministrative.dateDemarrageInscription.format(DATE_TIME_FORMAT)
        : undefined,
      dateAnnulationInscription: processusDinscriptionAdministrative.dateAnnulationInscription
        ? processusDinscriptionAdministrative.dateAnnulationInscription.format(DATE_TIME_FORMAT)
        : undefined,
      dateVisiteMedicale: processusDinscriptionAdministrative.dateVisiteMedicale
        ? processusDinscriptionAdministrative.dateVisiteMedicale.format(DATE_TIME_FORMAT)
        : undefined,
      dateRemiseQuitusCROUS: processusDinscriptionAdministrative.dateRemiseQuitusCROUS
        ? processusDinscriptionAdministrative.dateRemiseQuitusCROUS.format(DATE_TIME_FORMAT)
        : undefined,
      dateRemiseQuitusBU: processusDinscriptionAdministrative.dateRemiseQuitusBU
        ? processusDinscriptionAdministrative.dateRemiseQuitusBU.format(DATE_TIME_FORMAT)
        : undefined,
      datePaiementFraisObligatoires: processusDinscriptionAdministrative.datePaiementFraisObligatoires
        ? processusDinscriptionAdministrative.datePaiementFraisObligatoires.format(DATE_TIME_FORMAT)
        : undefined,
      dateRemiseCarteEtu: processusDinscriptionAdministrative.dateRemiseCarteEtu
        ? processusDinscriptionAdministrative.dateRemiseCarteEtu.format(DATE_TIME_FORMAT)
        : undefined,
      dateInscriptionAdministrative: processusDinscriptionAdministrative.dateInscriptionAdministrative
        ? processusDinscriptionAdministrative.dateInscriptionAdministrative.format(DATE_TIME_FORMAT)
        : undefined,
      derniereModification: processusDinscriptionAdministrative.derniereModification
        ? processusDinscriptionAdministrative.derniereModification.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
