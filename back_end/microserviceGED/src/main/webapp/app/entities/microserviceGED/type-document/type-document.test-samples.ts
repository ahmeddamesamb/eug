import { ITypeDocument, NewTypeDocument } from './type-document.model';

export const sampleWithRequiredData: ITypeDocument = {
  id: 13191,
};

export const sampleWithPartialData: ITypeDocument = {
  id: 10990,
};

export const sampleWithFullData: ITypeDocument = {
  id: 7096,
  libelleTypeDocument: 'respect',
};

export const sampleWithNewData: NewTypeDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
