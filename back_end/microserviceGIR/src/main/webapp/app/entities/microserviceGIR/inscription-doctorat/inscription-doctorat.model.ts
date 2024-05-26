import { IDoctorat } from 'app/entities/microserviceGIR/doctorat/doctorat.model';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';

export interface IInscriptionDoctorat {
  id: number;
  sourceFinancement?: string | null;
  coEncadreurId?: string | null;
  nombreInscription?: number | null;
  doctorat?: Pick<IDoctorat, 'id'> | null;
  inscriptionAdministrativeFormation?: Pick<IInscriptionAdministrativeFormation, 'id'> | null;
}

export type NewInscriptionDoctorat = Omit<IInscriptionDoctorat, 'id'> & { id: null };
