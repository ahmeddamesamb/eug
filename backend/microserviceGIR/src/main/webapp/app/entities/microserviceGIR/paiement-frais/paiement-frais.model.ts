import dayjs from 'dayjs/esm';
import { IFrais } from 'app/entities/microserviceGIR/frais/frais.model';
import { IOperateur } from 'app/entities/microserviceGIR/operateur/operateur.model';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';

export interface IPaiementFrais {
  id: number;
  datePaiement?: dayjs.Dayjs | null;
  obligatoireYN?: number | null;
  echeancePayeeYN?: number | null;
  emailUser?: string | null;
  frais?: Pick<IFrais, 'id'> | null;
  operateur?: Pick<IOperateur, 'id'> | null;
  inscriptionAdministrativeFormation?: Pick<IInscriptionAdministrativeFormation, 'id'> | null;
}

export type NewPaiementFrais = Omit<IPaiementFrais, 'id'> & { id: null };
