import { IPays, NewPays } from './pays.model';

export const sampleWithRequiredData: IPays = {
  id: 5719,
  libellePays: 'après-demain hormis parce que',
};

export const sampleWithPartialData: IPays = {
  id: 32124,
  libellePays: 'adepte population du Québec',
  paysEnAnglais: 'vlan ha ha',
  nationalite: 'trop',
  uEMOAYN: true,
  rIMYN: false,
  autreYN: true,
};

export const sampleWithFullData: IPays = {
  id: 15921,
  libellePays: 'durant',
  paysEnAnglais: 'spécialiste',
  nationalite: 'population du Québec',
  codePays: 'commis',
  uEMOAYN: false,
  cEDEAOYN: false,
  rIMYN: true,
  autreYN: true,
  estEtrangerYN: true,
};

export const sampleWithNewData: NewPays = {
  libellePays: 'tandis que',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
