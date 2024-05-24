import { IFormation, NewFormation } from './formation.model';

export const sampleWithRequiredData: IFormation = {
  id: 7906,
  typeFormation: 'PRIVEE',
};

export const sampleWithPartialData: IFormation = {
  id: 1111,
  classeDiplomanteYN: 30345,
  codeFormation: 'yowza',
  nbreCreditsMin: 15279,
  typeFormation: 'PRIVEE',
};

export const sampleWithFullData: IFormation = {
  id: 4143,
  fraisDossierYN: 16082,
  classeDiplomanteYN: 6070,
  libelleDiplome: 'wherever hm once',
  codeFormation: 'resell',
  nbreCreditsMin: 24827,
  estParcoursYN: 23348,
  lmdYN: 3048,
  typeFormation: 'PRIVEE',
};

export const sampleWithNewData: NewFormation = {
  typeFormation: 'PRIVEE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
