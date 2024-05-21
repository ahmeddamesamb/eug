import { ITypeHandicap, NewTypeHandicap } from './type-handicap.model';

export const sampleWithRequiredData: ITypeHandicap = {
  id: 9963,
  libelleTypeHandicap: 'enthusiastically',
};

export const sampleWithPartialData: ITypeHandicap = {
  id: 8879,
  libelleTypeHandicap: 'glass versus oddly',
};

export const sampleWithFullData: ITypeHandicap = {
  id: 8160,
  libelleTypeHandicap: 'yahoo summarize',
};

export const sampleWithNewData: NewTypeHandicap = {
  libelleTypeHandicap: 'phew',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
