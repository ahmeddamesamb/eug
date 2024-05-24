import { IPays, NewPays } from './pays.model';

export const sampleWithRequiredData: IPays = {
  id: 5719,
  libellePays: 'majestically anti furthermore',
};

export const sampleWithPartialData: IPays = {
  id: 32124,
  libellePays: 'backdrop tolerance',
  paysEnAnglais: 'ick gah',
  nationalite: 'unless',
  uEMOAYN: 16089,
  rIMYN: 20079,
  autreYN: 9241,
};

export const sampleWithFullData: IPays = {
  id: 15921,
  libellePays: 'among',
  paysEnAnglais: 'goodnight',
  nationalite: 'thickness',
  codePays: 'jumpsuit',
  uEMOAYN: 18809,
  cEDEAOYN: 22683,
  rIMYN: 14396,
  autreYN: 5034,
  estEtrangerYN: 12626,
};

export const sampleWithNewData: NewPays = {
  libellePays: 'why',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
