export interface IEnseignement {
  id: number;
  libelleEnseignements?: string | null;
  volumeHoraire?: number | null;
  nombreInscrits?: number | null;
  groupeYN?: boolean | null;
}

export type NewEnseignement = Omit<IEnseignement, 'id'> & { id: null };
