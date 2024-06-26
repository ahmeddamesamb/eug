import { ITypeHandicap, NewTypeHandicap } from './type-handicap.model';

export const sampleWithRequiredData: ITypeHandicap = {
  id: 9963,
  libelleTypeHandicap: 'approximativement',
};

export const sampleWithPartialData: ITypeHandicap = {
  id: 8879,
  libelleTypeHandicap: 'fourbe proche de déjà',
};

export const sampleWithFullData: ITypeHandicap = {
  id: 8160,
  libelleTypeHandicap: 'euh emmerder',
};

export const sampleWithNewData: NewTypeHandicap = {
  libelleTypeHandicap: 'pschitt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
