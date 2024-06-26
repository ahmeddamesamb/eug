import { ICycle } from 'app/entities/microserviceGIR/cycle/cycle.model';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface INiveau {
  id: number;
  libelleNiveau?: string | null;
  codeNiveau?: string | null;
  anneeEtude?: string | null;
  actifYN?: boolean | null;
  typeCycle?: Pick<ICycle, 'id'> | null;
  formations?: Pick<IFormation, 'id'>[] | null;
}

export type NewNiveau = Omit<INiveau, 'id'> & { id: null };
