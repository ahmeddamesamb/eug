import { IMention } from 'app/entities/microserviceGIR/mention/mention.model';

export interface ISpecialite {
  id: number;
  nomSpecialites?: string | null;
  sigleSpecialites?: string | null;
  specialiteParticulierYN?: number | null;
  specialitesPayanteYN?: number | null;
  mention?: Pick<IMention, 'id'> | null;
}

export type NewSpecialite = Omit<ISpecialite, 'id'> & { id: null };
