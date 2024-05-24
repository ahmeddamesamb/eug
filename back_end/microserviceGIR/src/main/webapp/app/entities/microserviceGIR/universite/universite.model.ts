import { IMinistere } from 'app/entities/microserviceGIR/ministere/ministere.model';

export interface IUniversite {
  id: number;
  nomUniversite?: string | null;
  sigleUniversite?: string | null;
  ministere?: Pick<IMinistere, 'id'> | null;
}

export type NewUniversite = Omit<IUniversite, 'id'> & { id: null };
