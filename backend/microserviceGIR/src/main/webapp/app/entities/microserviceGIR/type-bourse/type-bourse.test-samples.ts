import { ITypeBourse, NewTypeBourse } from './type-bourse.model';

export const sampleWithRequiredData: ITypeBourse = {
  id: 18963,
  libelleTypeBourse: 'quitte à partenaire',
};

export const sampleWithPartialData: ITypeBourse = {
  id: 9104,
  libelleTypeBourse: 'rassembler',
};

export const sampleWithFullData: ITypeBourse = {
  id: 19621,
  libelleTypeBourse: "tantôt à l'instar de",
};

export const sampleWithNewData: NewTypeBourse = {
  libelleTypeBourse: 'ouin sur clac',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
