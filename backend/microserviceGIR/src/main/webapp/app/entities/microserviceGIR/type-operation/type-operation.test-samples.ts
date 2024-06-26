import { ITypeOperation, NewTypeOperation } from './type-operation.model';

export const sampleWithRequiredData: ITypeOperation = {
  id: 2533,
  libelleTypeOperation: 'certes',
};

export const sampleWithPartialData: ITypeOperation = {
  id: 10169,
  libelleTypeOperation: 'sentir',
};

export const sampleWithFullData: ITypeOperation = {
  id: 19515,
  libelleTypeOperation: 'oups',
};

export const sampleWithNewData: NewTypeOperation = {
  libelleTypeOperation: 'souple',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
