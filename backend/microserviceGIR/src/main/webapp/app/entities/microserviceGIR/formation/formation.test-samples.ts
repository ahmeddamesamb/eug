import { IFormation, NewFormation } from './formation.model';

export const sampleWithRequiredData: IFormation = {
  id: 30864,
};

export const sampleWithPartialData: IFormation = {
  id: 21352,
  classeDiplomanteYN: false,
  nbreCreditsMin: 1111,
  lmdYN: false,
  actifYN: true,
};

export const sampleWithFullData: IFormation = {
  id: 1419,
  classeDiplomanteYN: true,
  libelleDiplome: 'blablabla en face de',
  codeFormation: 'passer crac',
  nbreCreditsMin: 21652,
  estParcoursYN: true,
  lmdYN: true,
  actifYN: true,
};

export const sampleWithNewData: NewFormation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
