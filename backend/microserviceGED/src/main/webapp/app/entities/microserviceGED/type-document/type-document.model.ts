export interface ITypeDocument {
  id: number;
  libelle?: string | null;
}

export type NewTypeDocument = Omit<ITypeDocument, 'id'> & { id: null };
