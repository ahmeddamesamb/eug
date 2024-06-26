import { IOperation } from 'app/entities/microserviceGIR/operation/operation.model';

export interface ITypeOperation {
  id: number;
  libelleTypeOperation?: string | null;
  operations?: Pick<IOperation, 'id'>[] | null;
}

export type NewTypeOperation = Omit<ITypeOperation, 'id'> & { id: null };
