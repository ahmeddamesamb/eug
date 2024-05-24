import { ILaboratoire, NewLaboratoire } from './laboratoire.model';

export const sampleWithRequiredData: ILaboratoire = {
  id: 14176,
};

export const sampleWithPartialData: ILaboratoire = {
  id: 19178,
  nomLaboratoire: 'cicada',
  laboratoireCotutelleYN: 3109,
};

export const sampleWithFullData: ILaboratoire = {
  id: 23528,
  nomLaboratoire: 'symptomize after oof',
  laboratoireCotutelleYN: 8618,
};

export const sampleWithNewData: NewLaboratoire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
