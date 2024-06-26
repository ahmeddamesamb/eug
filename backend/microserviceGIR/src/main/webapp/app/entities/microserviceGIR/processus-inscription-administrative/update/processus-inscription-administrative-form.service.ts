import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProcessusInscriptionAdministrative, NewProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcessusInscriptionAdministrative for edit and NewProcessusInscriptionAdministrativeFormGroupInput for create.
 */
type ProcessusInscriptionAdministrativeFormGroupInput =
  | IProcessusInscriptionAdministrative
  | PartialWithRequiredKeyOf<NewProcessusInscriptionAdministrative>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProcessusInscriptionAdministrative | NewProcessusInscriptionAdministrative> = Omit<
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

type ProcessusInscriptionAdministrativeFormRawValue = FormValueOf<IProcessusInscriptionAdministrative>;

type NewProcessusInscriptionAdministrativeFormRawValue = FormValueOf<NewProcessusInscriptionAdministrative>;

type ProcessusInscriptionAdministrativeFormDefaults = Pick<
  NewProcessusInscriptionAdministrative,
  | 'id'
  | 'inscriptionDemarreeYN'
  | 'dateDemarrageInscription'
  | 'inscriptionAnnuleeYN'
  | 'dateAnnulationInscription'
  | 'apteYN'
  | 'dateVisiteMedicale'
  | 'beneficiaireCROUSYN'
  | 'dateRemiseQuitusCROUS'
  | 'nouveauCodeBUYN'
  | 'quitusBUYN'
  | 'dateRemiseQuitusBU'
  | 'exporterBUYN'
  | 'fraisObligatoiresPayesYN'
  | 'datePaiementFraisObligatoires'
  | 'carteEturemiseYN'
  | 'carteDupremiseYN'
  | 'dateRemiseCarteEtu'
  | 'inscritAdministrativementYN'
  | 'dateInscriptionAdministrative'
  | 'derniereModification'
  | 'inscritOnlineYN'
>;

type ProcessusInscriptionAdministrativeFormGroupContent = {
  id: FormControl<ProcessusInscriptionAdministrativeFormRawValue['id'] | NewProcessusInscriptionAdministrative['id']>;
  inscriptionDemarreeYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['inscriptionDemarreeYN']>;
  dateDemarrageInscription: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateDemarrageInscription']>;
  inscriptionAnnuleeYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['inscriptionAnnuleeYN']>;
  dateAnnulationInscription: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateAnnulationInscription']>;
  apteYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['apteYN']>;
  dateVisiteMedicale: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateVisiteMedicale']>;
  beneficiaireCROUSYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['beneficiaireCROUSYN']>;
  dateRemiseQuitusCROUS: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateRemiseQuitusCROUS']>;
  nouveauCodeBUYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['nouveauCodeBUYN']>;
  quitusBUYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['quitusBUYN']>;
  dateRemiseQuitusBU: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateRemiseQuitusBU']>;
  exporterBUYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['exporterBUYN']>;
  fraisObligatoiresPayesYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['fraisObligatoiresPayesYN']>;
  datePaiementFraisObligatoires: FormControl<ProcessusInscriptionAdministrativeFormRawValue['datePaiementFraisObligatoires']>;
  numeroQuitusCROUS: FormControl<ProcessusInscriptionAdministrativeFormRawValue['numeroQuitusCROUS']>;
  carteEturemiseYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['carteEturemiseYN']>;
  carteDupremiseYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['carteDupremiseYN']>;
  dateRemiseCarteEtu: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateRemiseCarteEtu']>;
  dateRemiseCarteDup: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateRemiseCarteDup']>;
  inscritAdministrativementYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['inscritAdministrativementYN']>;
  dateInscriptionAdministrative: FormControl<ProcessusInscriptionAdministrativeFormRawValue['dateInscriptionAdministrative']>;
  derniereModification: FormControl<ProcessusInscriptionAdministrativeFormRawValue['derniereModification']>;
  inscritOnlineYN: FormControl<ProcessusInscriptionAdministrativeFormRawValue['inscritOnlineYN']>;
  emailUser: FormControl<ProcessusInscriptionAdministrativeFormRawValue['emailUser']>;
  inscriptionAdministrative: FormControl<ProcessusInscriptionAdministrativeFormRawValue['inscriptionAdministrative']>;
};

export type ProcessusInscriptionAdministrativeFormGroup = FormGroup<ProcessusInscriptionAdministrativeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcessusInscriptionAdministrativeFormService {
  createProcessusInscriptionAdministrativeFormGroup(
    processusInscriptionAdministrative: ProcessusInscriptionAdministrativeFormGroupInput = { id: null },
  ): ProcessusInscriptionAdministrativeFormGroup {
    const processusInscriptionAdministrativeRawValue =
      this.convertProcessusInscriptionAdministrativeToProcessusInscriptionAdministrativeRawValue({
        ...this.getFormDefaults(),
        ...processusInscriptionAdministrative,
      });
    return new FormGroup<ProcessusInscriptionAdministrativeFormGroupContent>({
      id: new FormControl(
        { value: processusInscriptionAdministrativeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      inscriptionDemarreeYN: new FormControl(processusInscriptionAdministrativeRawValue.inscriptionDemarreeYN),
      dateDemarrageInscription: new FormControl(processusInscriptionAdministrativeRawValue.dateDemarrageInscription),
      inscriptionAnnuleeYN: new FormControl(processusInscriptionAdministrativeRawValue.inscriptionAnnuleeYN),
      dateAnnulationInscription: new FormControl(processusInscriptionAdministrativeRawValue.dateAnnulationInscription),
      apteYN: new FormControl(processusInscriptionAdministrativeRawValue.apteYN),
      dateVisiteMedicale: new FormControl(processusInscriptionAdministrativeRawValue.dateVisiteMedicale),
      beneficiaireCROUSYN: new FormControl(processusInscriptionAdministrativeRawValue.beneficiaireCROUSYN),
      dateRemiseQuitusCROUS: new FormControl(processusInscriptionAdministrativeRawValue.dateRemiseQuitusCROUS),
      nouveauCodeBUYN: new FormControl(processusInscriptionAdministrativeRawValue.nouveauCodeBUYN),
      quitusBUYN: new FormControl(processusInscriptionAdministrativeRawValue.quitusBUYN),
      dateRemiseQuitusBU: new FormControl(processusInscriptionAdministrativeRawValue.dateRemiseQuitusBU),
      exporterBUYN: new FormControl(processusInscriptionAdministrativeRawValue.exporterBUYN),
      fraisObligatoiresPayesYN: new FormControl(processusInscriptionAdministrativeRawValue.fraisObligatoiresPayesYN),
      datePaiementFraisObligatoires: new FormControl(processusInscriptionAdministrativeRawValue.datePaiementFraisObligatoires),
      numeroQuitusCROUS: new FormControl(processusInscriptionAdministrativeRawValue.numeroQuitusCROUS),
      carteEturemiseYN: new FormControl(processusInscriptionAdministrativeRawValue.carteEturemiseYN),
      carteDupremiseYN: new FormControl(processusInscriptionAdministrativeRawValue.carteDupremiseYN),
      dateRemiseCarteEtu: new FormControl(processusInscriptionAdministrativeRawValue.dateRemiseCarteEtu),
      dateRemiseCarteDup: new FormControl(processusInscriptionAdministrativeRawValue.dateRemiseCarteDup),
      inscritAdministrativementYN: new FormControl(processusInscriptionAdministrativeRawValue.inscritAdministrativementYN),
      dateInscriptionAdministrative: new FormControl(processusInscriptionAdministrativeRawValue.dateInscriptionAdministrative),
      derniereModification: new FormControl(processusInscriptionAdministrativeRawValue.derniereModification),
      inscritOnlineYN: new FormControl(processusInscriptionAdministrativeRawValue.inscritOnlineYN),
      emailUser: new FormControl(processusInscriptionAdministrativeRawValue.emailUser),
      inscriptionAdministrative: new FormControl(processusInscriptionAdministrativeRawValue.inscriptionAdministrative),
    });
  }

  getProcessusInscriptionAdministrative(
    form: ProcessusInscriptionAdministrativeFormGroup,
  ): IProcessusInscriptionAdministrative | NewProcessusInscriptionAdministrative {
    return this.convertProcessusInscriptionAdministrativeRawValueToProcessusInscriptionAdministrative(
      form.getRawValue() as ProcessusInscriptionAdministrativeFormRawValue | NewProcessusInscriptionAdministrativeFormRawValue,
    );
  }

  resetForm(
    form: ProcessusInscriptionAdministrativeFormGroup,
    processusInscriptionAdministrative: ProcessusInscriptionAdministrativeFormGroupInput,
  ): void {
    const processusInscriptionAdministrativeRawValue =
      this.convertProcessusInscriptionAdministrativeToProcessusInscriptionAdministrativeRawValue({
        ...this.getFormDefaults(),
        ...processusInscriptionAdministrative,
      });
    form.reset(
      {
        ...processusInscriptionAdministrativeRawValue,
        id: { value: processusInscriptionAdministrativeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProcessusInscriptionAdministrativeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inscriptionDemarreeYN: false,
      dateDemarrageInscription: currentTime,
      inscriptionAnnuleeYN: false,
      dateAnnulationInscription: currentTime,
      apteYN: false,
      dateVisiteMedicale: currentTime,
      beneficiaireCROUSYN: false,
      dateRemiseQuitusCROUS: currentTime,
      nouveauCodeBUYN: false,
      quitusBUYN: false,
      dateRemiseQuitusBU: currentTime,
      exporterBUYN: false,
      fraisObligatoiresPayesYN: false,
      datePaiementFraisObligatoires: currentTime,
      carteEturemiseYN: false,
      carteDupremiseYN: false,
      dateRemiseCarteEtu: currentTime,
      inscritAdministrativementYN: false,
      dateInscriptionAdministrative: currentTime,
      derniereModification: currentTime,
      inscritOnlineYN: false,
    };
  }

  private convertProcessusInscriptionAdministrativeRawValueToProcessusInscriptionAdministrative(
    rawProcessusInscriptionAdministrative:
      | ProcessusInscriptionAdministrativeFormRawValue
      | NewProcessusInscriptionAdministrativeFormRawValue,
  ): IProcessusInscriptionAdministrative | NewProcessusInscriptionAdministrative {
    return {
      ...rawProcessusInscriptionAdministrative,
      dateDemarrageInscription: dayjs(rawProcessusInscriptionAdministrative.dateDemarrageInscription, DATE_TIME_FORMAT),
      dateAnnulationInscription: dayjs(rawProcessusInscriptionAdministrative.dateAnnulationInscription, DATE_TIME_FORMAT),
      dateVisiteMedicale: dayjs(rawProcessusInscriptionAdministrative.dateVisiteMedicale, DATE_TIME_FORMAT),
      dateRemiseQuitusCROUS: dayjs(rawProcessusInscriptionAdministrative.dateRemiseQuitusCROUS, DATE_TIME_FORMAT),
      dateRemiseQuitusBU: dayjs(rawProcessusInscriptionAdministrative.dateRemiseQuitusBU, DATE_TIME_FORMAT),
      datePaiementFraisObligatoires: dayjs(rawProcessusInscriptionAdministrative.datePaiementFraisObligatoires, DATE_TIME_FORMAT),
      dateRemiseCarteEtu: dayjs(rawProcessusInscriptionAdministrative.dateRemiseCarteEtu, DATE_TIME_FORMAT),
      dateInscriptionAdministrative: dayjs(rawProcessusInscriptionAdministrative.dateInscriptionAdministrative, DATE_TIME_FORMAT),
      derniereModification: dayjs(rawProcessusInscriptionAdministrative.derniereModification, DATE_TIME_FORMAT),
    };
  }

  private convertProcessusInscriptionAdministrativeToProcessusInscriptionAdministrativeRawValue(
    processusInscriptionAdministrative:
      | IProcessusInscriptionAdministrative
      | (Partial<NewProcessusInscriptionAdministrative> & ProcessusInscriptionAdministrativeFormDefaults),
  ): ProcessusInscriptionAdministrativeFormRawValue | PartialWithRequiredKeyOf<NewProcessusInscriptionAdministrativeFormRawValue> {
    return {
      ...processusInscriptionAdministrative,
      dateDemarrageInscription: processusInscriptionAdministrative.dateDemarrageInscription
        ? processusInscriptionAdministrative.dateDemarrageInscription.format(DATE_TIME_FORMAT)
        : undefined,
      dateAnnulationInscription: processusInscriptionAdministrative.dateAnnulationInscription
        ? processusInscriptionAdministrative.dateAnnulationInscription.format(DATE_TIME_FORMAT)
        : undefined,
      dateVisiteMedicale: processusInscriptionAdministrative.dateVisiteMedicale
        ? processusInscriptionAdministrative.dateVisiteMedicale.format(DATE_TIME_FORMAT)
        : undefined,
      dateRemiseQuitusCROUS: processusInscriptionAdministrative.dateRemiseQuitusCROUS
        ? processusInscriptionAdministrative.dateRemiseQuitusCROUS.format(DATE_TIME_FORMAT)
        : undefined,
      dateRemiseQuitusBU: processusInscriptionAdministrative.dateRemiseQuitusBU
        ? processusInscriptionAdministrative.dateRemiseQuitusBU.format(DATE_TIME_FORMAT)
        : undefined,
      datePaiementFraisObligatoires: processusInscriptionAdministrative.datePaiementFraisObligatoires
        ? processusInscriptionAdministrative.datePaiementFraisObligatoires.format(DATE_TIME_FORMAT)
        : undefined,
      dateRemiseCarteEtu: processusInscriptionAdministrative.dateRemiseCarteEtu
        ? processusInscriptionAdministrative.dateRemiseCarteEtu.format(DATE_TIME_FORMAT)
        : undefined,
      dateInscriptionAdministrative: processusInscriptionAdministrative.dateInscriptionAdministrative
        ? processusInscriptionAdministrative.dateInscriptionAdministrative.format(DATE_TIME_FORMAT)
        : undefined,
      derniereModification: processusInscriptionAdministrative.derniereModification
        ? processusInscriptionAdministrative.derniereModification.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
