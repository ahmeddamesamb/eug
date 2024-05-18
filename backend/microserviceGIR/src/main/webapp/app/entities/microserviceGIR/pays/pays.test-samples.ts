import { IPays, NewPays } from './pays.model';

export const sampleWithRequiredData: IPays = {
  id: 5719,
};

export const sampleWithPartialData: IPays = {
  id: 1987,
  paysEnAnglais: 'fooey pace',
  nationalite: 'immediately hm',
  uEMOAYN: 7028,
  cEDEAOYN: 19771,
  rIMYN: 10495,
  autreYN: 8911,
  estEtrangerYN: 6851,
};

export const sampleWithFullData: IPays = {
  id: 29895,
  libellePays: 'ick gah',
  paysEnAnglais: 'unless',
  nationalite: 'behind onto',
  codePays: 'grocery bravely oppose',
  uEMOAYN: 22683,
  cEDEAOYN: 14396,
  rIMYN: 5034,
  autreYN: 12626,
  estEtrangerYN: 137,
};

export const sampleWithNewData: NewPays = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
