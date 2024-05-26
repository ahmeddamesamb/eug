import { IAnneeAcademique, NewAnneeAcademique } from './annee-academique.model';

export const sampleWithRequiredData: IAnneeAcademique = {
  id: 18259,
  libelleAnneeAcademique: 'jaggedly seek speedboat',
  anneeAc: 'neve',
};

export const sampleWithPartialData: IAnneeAcademique = {
  id: 13484,
  libelleAnneeAcademique: 'yippee',
  anneeAc: 'meh ',
  anneeCouranteYN: 3707,
};

export const sampleWithFullData: IAnneeAcademique = {
  id: 19916,
  libelleAnneeAcademique: 'wide fast',
  anneeAc: 'frig',
  anneeCouranteYN: 18113,
};

export const sampleWithNewData: NewAnneeAcademique = {
  libelleAnneeAcademique: 'strictly',
  anneeAc: 'hmph',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
