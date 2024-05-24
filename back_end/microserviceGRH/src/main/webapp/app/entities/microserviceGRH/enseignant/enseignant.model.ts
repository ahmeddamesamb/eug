export interface IEnseignant {
  id: number;
  titreCoEncadreur?: string | null;
  nomEnseignant?: string | null;
  prenomEnseignant?: string | null;
  emailEnseignant?: string | null;
  telephoneEnseignant?: string | null;
  titresId?: number | null;
  adresse?: string | null;
  numeroPoste?: number | null;
  photoEnseignant?: string | null;
  photoEnseignantContentType?: string | null;
}

export type NewEnseignant = Omit<IEnseignant, 'id'> & { id: null };
