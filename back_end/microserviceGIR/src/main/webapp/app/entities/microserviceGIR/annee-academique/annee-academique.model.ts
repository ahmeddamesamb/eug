export interface IAnneeAcademique {
  id: number;
  libelleAnneeAcademique?: string | null;
  anneeAc?: string | null;
  anneeCouranteYN?: number | null;
}

export type NewAnneeAcademique = Omit<IAnneeAcademique, 'id'> & { id: null };
