import { ITypeBourse, NewTypeBourse } from './type-bourse.model';

export const sampleWithRequiredData: ITypeBourse = {
  id: 18963,
};

export const sampleWithPartialData: ITypeBourse = {
  id: 12823,
  libelle: 'brace defiantly',
};

export const sampleWithFullData: ITypeBourse = {
  id: 4112,
  libelle: 'heavy',
};

export const sampleWithNewData: NewTypeBourse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
