export interface IEnseignant {
  id: number;
  titreCoEncadreur?: string | null;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  telephone?: string | null;
  titresId?: number | null;
  adresse?: string | null;
  numeroPoste?: number | null;
  photo?: string | null;
}

export type NewEnseignant = Omit<IEnseignant, 'id'> & { id: null };
