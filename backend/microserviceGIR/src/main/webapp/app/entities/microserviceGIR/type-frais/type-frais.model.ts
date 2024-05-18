export interface ITypeFrais {
  id: number;
  libelle?: string | null;
}

export type NewTypeFrais = Omit<ITypeFrais, 'id'> & { id: null };
