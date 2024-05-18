import { IPays } from 'app/entities/microserviceGIR/pays/pays.model';

export interface IRegion {
  id: number;
  libelleRegion?: string | null;
  pays?: Pick<IPays, 'id'> | null;
}

export type NewRegion = Omit<IRegion, 'id'> & { id: null };
