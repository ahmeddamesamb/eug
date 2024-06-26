export interface ILaboratoire {
  id: number;
  nom?: string | null;
  laboratoireCotutelleYN?: boolean | null;
}

export type NewLaboratoire = Omit<ILaboratoire, 'id'> & { id: null };
