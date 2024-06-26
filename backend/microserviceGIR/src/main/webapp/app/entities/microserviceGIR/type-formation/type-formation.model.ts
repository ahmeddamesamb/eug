import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface ITypeFormation {
  id: number;
  libelleTypeFormation?: string | null;
  formations?: Pick<IFormation, 'id'>[] | null;
}

export type NewTypeFormation = Omit<ITypeFormation, 'id'> & { id: null };
