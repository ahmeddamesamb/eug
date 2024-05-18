import dayjs from 'dayjs/esm';
import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface IInscriptionAdministrativeFormation {
  id: number;
  inscriptionPrincipaleYN?: number | null;
  inscriptionAnnuleeYN?: number | null;
  paiementFraisOblYN?: number | null;
  paiementFraisIntegergYN?: number | null;
  certificatDelivreYN?: number | null;
  dateChoixFormation?: dayjs.Dayjs | null;
  dateValidationInscription?: dayjs.Dayjs | null;
  inscriptionAdministrative?: Pick<IInscriptionAdministrative, 'id'> | null;
  formation?: Pick<IFormation, 'id'> | null;
}

export type NewInscriptionAdministrativeFormation = Omit<IInscriptionAdministrativeFormation, 'id'> & { id: null };
