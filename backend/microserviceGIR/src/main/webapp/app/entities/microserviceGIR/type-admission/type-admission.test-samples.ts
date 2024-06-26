import { ITypeAdmission, NewTypeAdmission } from './type-admission.model';

export const sampleWithRequiredData: ITypeAdmission = {
  id: 11883,
  libelleTypeAdmission: 'prestataire de services apaiser',
};

export const sampleWithPartialData: ITypeAdmission = {
  id: 24616,
  libelleTypeAdmission: 'atchoum',
};

export const sampleWithFullData: ITypeAdmission = {
  id: 29613,
  libelleTypeAdmission: 'vroum pendant que',
};

export const sampleWithNewData: NewTypeAdmission = {
  libelleTypeAdmission: 'placide rédaction lorsque',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
