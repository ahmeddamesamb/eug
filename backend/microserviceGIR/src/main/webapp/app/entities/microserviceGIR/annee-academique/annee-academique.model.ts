export interface IAnneeAcademique {
  id: number;
  libelleAnneeAcademique?: string | null;
  anneeCourante?: number | null;
}

export type NewAnneeAcademique = Omit<IAnneeAcademique, 'id'> & { id: null };
