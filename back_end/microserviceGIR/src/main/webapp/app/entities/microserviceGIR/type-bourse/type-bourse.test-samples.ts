import { ITypeBourse, NewTypeBourse } from './type-bourse.model';

export const sampleWithRequiredData: ITypeBourse = {
  id: 18963,
  libelleTypeBourse: 'vice comeback',
};

export const sampleWithPartialData: ITypeBourse = {
  id: 9104,
  libelleTypeBourse: 'inherit',
};

export const sampleWithFullData: ITypeBourse = {
  id: 19621,
  libelleTypeBourse: 'safely from',
};

export const sampleWithNewData: NewTypeBourse = {
  libelleTypeBourse: 'bah beyond geez',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
