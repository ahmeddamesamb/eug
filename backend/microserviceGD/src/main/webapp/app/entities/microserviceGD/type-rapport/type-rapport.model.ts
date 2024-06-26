export interface ITypeRapport {
  id: number;
  libelleTypeRapport?: string | null;
}

export type NewTypeRapport = Omit<ITypeRapport, 'id'> & { id: null };
