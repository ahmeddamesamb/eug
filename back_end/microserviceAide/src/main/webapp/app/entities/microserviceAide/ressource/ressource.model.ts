export interface IRessource {
  id: number;
  nomRessource?: string | null;
  descriptionRessource?: string | null;
}

export type NewRessource = Omit<IRessource, 'id'> & { id: null };
