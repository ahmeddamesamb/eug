import { IEnseignant, NewEnseignant } from './enseignant.model';

export const sampleWithRequiredData: IEnseignant = {
  id: 12789,
  nomEnseignant: 'since',
  prenomEnseignant: 'kilt wiretap',
};

export const sampleWithPartialData: IEnseignant = {
  id: 12046,
  titreCoEncadreur: 'yippee',
  nomEnseignant: 'twin',
  prenomEnseignant: 'plus',
  titresId: 25443,
  adresse: 'greedily',
  numeroPoste: 7088,
};

export const sampleWithFullData: IEnseignant = {
  id: 6605,
  titreCoEncadreur: 'butter extremist separately',
  nomEnseignant: 'jovial mid',
  prenomEnseignant: 'including equally beside',
  emailEnseignant: 'midst suddenly unburden',
  telephoneEnseignant: 'so',
  titresId: 15757,
  adresse: 'issue',
  numeroPoste: 12454,
  photoEnseignant: '../fake-data/blob/hipster.png',
  photoEnseignantContentType: 'unknown',
};

export const sampleWithNewData: NewEnseignant = {
  nomEnseignant: 'drat before to',
  prenomEnseignant: 'contour budget',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
