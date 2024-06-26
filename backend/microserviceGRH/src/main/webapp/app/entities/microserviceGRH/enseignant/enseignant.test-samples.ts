import { IEnseignant, NewEnseignant } from './enseignant.model';

export const sampleWithRequiredData: IEnseignant = {
  id: 12789,
  nom: 'si bien que',
  prenom: 'commis main-d’œuvre',
};

export const sampleWithPartialData: IEnseignant = {
  id: 12046,
  titreCoEncadreur: 'oh',
  nom: 'anéantir',
  prenom: 'de façon à',
  titresId: 25443,
  adresse: 'joliment',
  numeroPoste: 7088,
};

export const sampleWithFullData: IEnseignant = {
  id: 6605,
  titreCoEncadreur: 'définir chef ailleurs',
  nom: 'insipide à travers',
  prenom: 'à même beaucoup selon',
  email: 'Pierrick85@yahoo.fr',
  telephone: '0650777637',
  titresId: 19402,
  adresse: 'fonctionnaire jeune prout',
  numeroPoste: 15424,
  photo: 'tant que aussitôt que',
};

export const sampleWithNewData: NewEnseignant = {
  nom: 'en guise de',
  prenom: 'collègue essayer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
