export interface IDisciplineSportive {
  id: number;
  libelleDisciplineSportive?: string | null;
}

export type NewDisciplineSportive = Omit<IDisciplineSportive, 'id'> & { id: null };
