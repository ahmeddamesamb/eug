export interface IOperateur {
  id: number;
  libelle?: string | null;
  userLogin?: string | null;
  codeOperateur?: string | null;
}

export type NewOperateur = Omit<IOperateur, 'id'> & { id: null };
