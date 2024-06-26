import dayjs from 'dayjs/esm';
import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { IInscriptionDoctorat } from 'app/entities/microserviceGIR/inscription-doctorat/inscription-doctorat.model';
import { IPaiementFrais } from 'app/entities/microserviceGIR/paiement-frais/paiement-frais.model';
import { IPaiementFormationPrivee } from 'app/entities/microserviceGIR/paiement-formation-privee/paiement-formation-privee.model';

export interface IInscriptionAdministrativeFormation {
  id: number;
  inscriptionPrincipaleYN?: boolean | null;
  inscriptionAnnuleeYN?: boolean | null;
  exonereYN?: boolean | null;
  paiementFraisOblYN?: boolean | null;
  paiementFraisIntegergYN?: boolean | null;
  certificatDelivreYN?: boolean | null;
  dateChoixFormation?: dayjs.Dayjs | null;
  dateValidationInscription?: dayjs.Dayjs | null;
  inscriptionAdministrative?: Pick<IInscriptionAdministrative, 'id'> | null;
  formation?: Pick<IFormation, 'id'> | null;
  inscriptionDoctorats?: Pick<IInscriptionDoctorat, 'id'>[] | null;
  paiementFrais?: Pick<IPaiementFrais, 'id'>[] | null;
  paiementFormationPrivees?: Pick<IPaiementFormationPrivee, 'id'>[] | null;
}

export type NewInscriptionAdministrativeFormation = Omit<IInscriptionAdministrativeFormation, 'id'> & { id: null };
