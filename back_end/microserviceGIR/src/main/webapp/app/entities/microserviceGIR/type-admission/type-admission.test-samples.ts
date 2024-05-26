import { ITypeAdmission, NewTypeAdmission } from './type-admission.model';

export const sampleWithRequiredData: ITypeAdmission = {
  id: 11883,
  libelleTypeAdmission: 'hamburger propitiate',
};

export const sampleWithPartialData: ITypeAdmission = {
  id: 24616,
  libelleTypeAdmission: 'phooey',
};

export const sampleWithFullData: ITypeAdmission = {
  id: 29613,
  libelleTypeAdmission: 'oof who',
};

export const sampleWithNewData: NewTypeAdmission = {
  libelleTypeAdmission: 'reckless twig whenever',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
