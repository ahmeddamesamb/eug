import { ITypeAdmission, NewTypeAdmission } from './type-admission.model';

export const sampleWithRequiredData: ITypeAdmission = {
  id: 11883,
};

export const sampleWithPartialData: ITypeAdmission = {
  id: 4583,
};

export const sampleWithFullData: ITypeAdmission = {
  id: 8077,
  libelle: 'winged circuit',
};

export const sampleWithNewData: NewTypeAdmission = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
