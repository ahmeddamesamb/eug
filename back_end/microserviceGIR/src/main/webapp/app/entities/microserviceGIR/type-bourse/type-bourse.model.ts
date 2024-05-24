export interface ITypeBourse {
  id: number;
  libelleTypeBourse?: string | null;
}

export type NewTypeBourse = Omit<ITypeBourse, 'id'> & { id: null };
