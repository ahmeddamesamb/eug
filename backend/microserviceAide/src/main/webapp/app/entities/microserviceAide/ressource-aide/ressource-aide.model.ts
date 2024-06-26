export interface IRessourceAide {
  id: number;
  nom?: string | null;
  libelle?: string | null;
}

export type NewRessourceAide = Omit<IRessourceAide, 'id'> & { id: null };
