import { Cycle } from 'app/entities/enumerations/cycle.model';

export interface INiveau {
  id: number;
  libelleNiveau?: string | null;
  cycleNiveau?: keyof typeof Cycle | null;
  codeNiveau?: string | null;
  anneeEtude?: string | null;
}

export type NewNiveau = Omit<INiveau, 'id'> & { id: null };
