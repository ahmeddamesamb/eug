import { IPaiementFrais } from 'app/entities/microserviceGIR/paiement-frais/paiement-frais.model';
import { IPaiementFormationPrivee } from 'app/entities/microserviceGIR/paiement-formation-privee/paiement-formation-privee.model';

export interface IOperateur {
  id: number;
  libelleOperateur?: string | null;
  userLogin?: string | null;
  codeOperateur?: string | null;
  actifYN?: boolean | null;
  paiementFrais?: Pick<IPaiementFrais, 'id'>[] | null;
  paiementFormationPrivees?: Pick<IPaiementFormationPrivee, 'id'>[] | null;
}

export type NewOperateur = Omit<IOperateur, 'id'> & { id: null };
