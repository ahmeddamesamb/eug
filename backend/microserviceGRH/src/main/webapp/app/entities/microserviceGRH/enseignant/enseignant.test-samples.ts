import { IEnseignant, NewEnseignant } from './enseignant.model';

export const sampleWithRequiredData: IEnseignant = {
  id: 7208,
  nomEnseignant: 'paltry',
  prenomEnseignant: 'tower during',
};

export const sampleWithPartialData: IEnseignant = {
  id: 23666,
  nomEnseignant: 'chafe astride plus',
  prenomEnseignant: 'far-flung sleepy alongside',
  telephoneEnseignant: 'curry',
  titresId: 1008,
  utilisateursId: 14518,
  numeroPoste: 23550,
  photoEnseignant: '../fake-data/blob/hipster.png',
  photoEnseignantContentType: 'unknown',
};

export const sampleWithFullData: IEnseignant = {
  id: 15758,
  titreCoEncadreur: 'choke outside including',
  nomEnseignant: 'limply tensely midst',
  prenomEnseignant: 'near',
  emailEnseignant: 'airport lean ouch',
  telephoneEnseignant: 'verbally wisely',
  titresId: 26764,
  utilisateursId: 8951,
  adresse: 'lest',
  numeroPoste: 24,
  photoEnseignant: '../fake-data/blob/hipster.png',
  photoEnseignantContentType: 'unknown',
};

export const sampleWithNewData: NewEnseignant = {
  nomEnseignant: 'oof likewise',
  prenomEnseignant: 'as provided',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
