import { ITypeOperation, NewTypeOperation } from './type-operation.model';

export const sampleWithRequiredData: ITypeOperation = {
  id: 2533,
  libelleTypeOperation: 'violently',
};

export const sampleWithPartialData: ITypeOperation = {
  id: 10169,
  libelleTypeOperation: 'belay',
};

export const sampleWithFullData: ITypeOperation = {
  id: 19515,
  libelleTypeOperation: 'gadzooks',
};

export const sampleWithNewData: NewTypeOperation = {
  libelleTypeOperation: 'tangible',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
