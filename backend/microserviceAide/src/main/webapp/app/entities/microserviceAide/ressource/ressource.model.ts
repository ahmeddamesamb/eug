export interface IRessource {
  id: number;
  nom?: string | null;
  description?: string | null;
}

export type NewRessource = Omit<IRessource, 'id'> & { id: null };
