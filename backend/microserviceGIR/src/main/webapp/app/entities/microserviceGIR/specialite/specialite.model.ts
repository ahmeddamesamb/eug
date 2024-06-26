import { IMention } from 'app/entities/microserviceGIR/mention/mention.model';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface ISpecialite {
  id: number;
  nomSpecialites?: string | null;
  sigleSpecialites?: string | null;
  specialiteParticulierYN?: boolean | null;
  specialitesPayanteYN?: boolean | null;
  actifYN?: boolean | null;
  mention?: Pick<IMention, 'id'> | null;
  formations?: Pick<IFormation, 'id'>[] | null;
}

export type NewSpecialite = Omit<ISpecialite, 'id'> & { id: null };
