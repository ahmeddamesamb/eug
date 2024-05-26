export interface IEnseignement {
  id: number;
  libelleEnseignements?: string | null;
  volumeHoraire?: number | null;
  nombreInscrits?: number | null;
  groupeYN?: number | null;
}

export type NewEnseignement = Omit<IEnseignement, 'id'> & { id: null };
