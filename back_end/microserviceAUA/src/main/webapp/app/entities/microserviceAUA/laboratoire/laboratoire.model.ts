export interface ILaboratoire {
  id: number;
  nomLaboratoire?: string | null;
  laboratoireCotutelleYN?: number | null;
}

export type NewLaboratoire = Omit<ILaboratoire, 'id'> & { id: null };
