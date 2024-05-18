import { IPays } from 'app/entities/microserviceGIR/pays/pays.model';

export interface IZone {
  id: number;
  libelleZone?: string | null;
  pays?: Pick<IPays, 'id'>[] | null;
}

export type NewZone = Omit<IZone, 'id'> & { id: null };
