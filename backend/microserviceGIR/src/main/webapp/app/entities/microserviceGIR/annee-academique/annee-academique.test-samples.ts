import { IAnneeAcademique, NewAnneeAcademique } from './annee-academique.model';

export const sampleWithRequiredData: IAnneeAcademique = {
  id: 5754,
};

export const sampleWithPartialData: IAnneeAcademique = {
  id: 20603,
};

export const sampleWithFullData: IAnneeAcademique = {
  id: 25075,
  libelleAnneeAcademique: 'though hopelessly overplay',
  anneeCourante: 16665,
};

export const sampleWithNewData: NewAnneeAcademique = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
