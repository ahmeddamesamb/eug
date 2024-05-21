import { ITypeOperation, NewTypeOperation } from './type-operation.model';

export const sampleWithRequiredData: ITypeOperation = {
  id: 2533,
};

export const sampleWithPartialData: ITypeOperation = {
  id: 31914,
  libelleTypeOperation: 'boohoo belay',
};

export const sampleWithFullData: ITypeOperation = {
  id: 19515,
  libelleTypeOperation: 'gadzooks',
};

export const sampleWithNewData: NewTypeOperation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
