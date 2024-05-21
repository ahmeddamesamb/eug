export interface INiveau {
  id: number;
  libelleNiveau?: string | null;
  cycleNiveau?: string | null;
  codeNiveau?: string | null;
  anneeEtude?: string | null;
}

export type NewNiveau = Omit<INiveau, 'id'> & { id: null };
