import { IRessource, NewRessource } from './ressource.model';

export const sampleWithRequiredData: IRessource = {
  id: 14286,
};

export const sampleWithPartialData: IRessource = {
  id: 19249,
  descriptionRessource: 'bracket',
};

export const sampleWithFullData: IRessource = {
  id: 16152,
  nomRessource: 'ew oh',
  descriptionRessource: 'mysteriously from',
};

export const sampleWithNewData: NewRessource = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
