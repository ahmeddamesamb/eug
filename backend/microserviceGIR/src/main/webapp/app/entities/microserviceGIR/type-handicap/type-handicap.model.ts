export interface ITypeHandicap {
  id: number;
  libelleTypeHandicap?: string | null;
}

export type NewTypeHandicap = Omit<ITypeHandicap, 'id'> & { id: null };
