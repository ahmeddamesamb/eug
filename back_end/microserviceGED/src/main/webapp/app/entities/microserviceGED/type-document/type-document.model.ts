export interface ITypeDocument {
  id: number;
  libelleTypeDocument?: string | null;
}

export type NewTypeDocument = Omit<ITypeDocument, 'id'> & { id: null };
