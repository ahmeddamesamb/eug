export interface ITypeFrais {
  id: number;
  libelleTypeFrais?: string | null;
}

export type NewTypeFrais = Omit<ITypeFrais, 'id'> & { id: null };
