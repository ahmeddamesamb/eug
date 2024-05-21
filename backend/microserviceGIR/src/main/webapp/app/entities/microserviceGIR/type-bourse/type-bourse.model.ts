export interface ITypeBourse {
  id: number;
  libelle?: string | null;
}

export type NewTypeBourse = Omit<ITypeBourse, 'id'> & { id: null };
