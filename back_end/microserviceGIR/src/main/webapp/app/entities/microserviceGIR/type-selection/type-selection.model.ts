export interface ITypeSelection {
  id: number;
  libelleTypeSelection?: string | null;
}

export type NewTypeSelection = Omit<ITypeSelection, 'id'> & { id: null };
