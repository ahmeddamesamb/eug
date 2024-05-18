export interface ITypeSelection {
  id: number;
  libelle?: string | null;
}

export type NewTypeSelection = Omit<ITypeSelection, 'id'> & { id: null };
