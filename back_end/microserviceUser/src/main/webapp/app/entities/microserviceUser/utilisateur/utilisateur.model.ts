export interface IUtilisateur {
  id: number;
  nomUser?: string | null;
  prenomUser?: string | null;
  emailUser?: string | null;
  motDePasse?: string | null;
  role?: string | null;
  matricule?: string | null;
  departement?: string | null;
  statut?: string | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
