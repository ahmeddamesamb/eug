import { IFormation, NewFormation } from './formation.model';

export const sampleWithRequiredData: IFormation = {
  id: 26768,
};

export const sampleWithPartialData: IFormation = {
  id: 7108,
  classeDiplomanteYN: 21352,
  nbreCreditsMin: 19103,
  lmdYN: 1111,
};

export const sampleWithFullData: IFormation = {
  id: 30345,
  nombreMensualites: 8093,
  fraisDossierYN: 1419,
  classeDiplomanteYN: 3356,
  libelleDiplome: 'whoa till',
  codeFormation: 'adapt yum',
  nbreCreditsMin: 21652,
  estParcoursYN: 16137,
  lmdYN: 5908,
};

export const sampleWithNewData: NewFormation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
