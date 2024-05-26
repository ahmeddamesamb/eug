export interface ITypeOperation {
  id: number;
  libelleTypeOperation?: string | null;
}

export type NewTypeOperation = Omit<ITypeOperation, 'id'> & { id: null };
