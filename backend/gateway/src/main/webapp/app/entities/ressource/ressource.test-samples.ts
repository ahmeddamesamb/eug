import { IRessource, NewRessource } from './ressource.model';

export const sampleWithRequiredData: IRessource = {
  id: 24017,
  libelle: 'candide',
  actifYN: true,
};

export const sampleWithPartialData: IRessource = {
  id: 31205,
  libelle: 'effrayer',
  actifYN: true,
};

export const sampleWithFullData: IRessource = {
  id: 18160,
  libelle: 'fixer recommander',
  actifYN: false,
};

export const sampleWithNewData: NewRessource = {
  libelle: "d'abord aux alentours de neutre",
  actifYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
