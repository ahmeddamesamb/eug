import dayjs from 'dayjs/esm';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { IOperateur } from 'app/entities/microserviceGIR/operateur/operateur.model';

export interface IPaiementFormationPrivee {
  id: number;
  datePaiement?: dayjs.Dayjs | null;
  moisPaiement?: string | null;
  anneePaiement?: string | null;
  payerMensualiteYN?: number | null;
  emailUser?: string | null;
  inscriptionAdministrativeFormation?: Pick<IInscriptionAdministrativeFormation, 'id'> | null;
  operateur?: Pick<IOperateur, 'id'> | null;
}

export type NewPaiementFormationPrivee = Omit<IPaiementFormationPrivee, 'id'> & { id: null };
