import { IUniversite, NewUniversite } from './universite.model';

export const sampleWithRequiredData: IUniversite = {
  id: 26752,
  nomUniversite: 'cependant',
  sigleUniversite: "à l'égard de tout pis",
  actifYN: true,
};

export const sampleWithPartialData: IUniversite = {
  id: 15753,
  nomUniversite: 'aux alentours de',
  sigleUniversite: 'tant que de façon que',
  actifYN: false,
};

export const sampleWithFullData: IUniversite = {
  id: 15463,
  nomUniversite: 'arracher areu areu commissionnaire',
  sigleUniversite: 'guide partout opposer',
  actifYN: false,
};

export const sampleWithNewData: NewUniversite = {
  nomUniversite: 'entreprendre',
  sigleUniversite: 'quasiment',
  actifYN: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
