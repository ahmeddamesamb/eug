import { ILaboratoire, NewLaboratoire } from './laboratoire.model';

export const sampleWithRequiredData: ILaboratoire = {
  id: 14176,
};

export const sampleWithPartialData: ILaboratoire = {
  id: 19178,
  nom: 'responsable',
  laboratoireCotutelleYN: true,
};

export const sampleWithFullData: ILaboratoire = {
  id: 23528,
  nom: 'conformer que vroum',
  laboratoireCotutelleYN: true,
};

export const sampleWithNewData: NewLaboratoire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
