import {FormationModel} from '../../parametrage/components/formation/models/formation-model';
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
  formation?: FormationModel
}
