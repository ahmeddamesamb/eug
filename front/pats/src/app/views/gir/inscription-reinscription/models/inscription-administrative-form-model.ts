import {FormationModel} from '../../parametrage/components/formation/models/formation-model';
import { NiveauModel } from '../../parametrage/components/niveau/models/niveau-model';
import { TypeadmissionModel } from '../../parametrage/components/typeadmission/models/typeadmission-model';
import { UfrModel } from '../../parametrage/components/ufr/models/ufr-model';
import {InscriptionAdministrativeModel} from '../models/inscription-administrative-model';

export interface InscriptionAdministrativeFormModel {
  id: number;
  inscriptionPrincipaleYN?: boolean | null;
  inscriptionAnnuleeYN?: boolean | null;
  exonereYN?: boolean | null;
  paiementFraisOblYN?: boolean | null;
  paiementFraisIntegergYN?: boolean | null;
  certificatDelivreYN?: boolean | null;
  dateChoixFormation?: Date | null;
  dateValidationInscription?: Date | null;
  inscriptionAdministrative?: InscriptionAdministrativeModel;
  formation?: FormationModel,
  ufr?: UfrModel,
  niveau?: NiveauModel,
  typeAdmission?: TypeadmissionModel
}
