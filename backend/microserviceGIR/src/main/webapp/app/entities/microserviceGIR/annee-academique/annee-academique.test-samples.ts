import { IAnneeAcademique, NewAnneeAcademique } from './annee-academique.model';

export const sampleWithRequiredData: IAnneeAcademique = {
  id: 30403,
  libelleAnneeAcademique: 'environ camarade',
  anneeAc: 1990,
  separateur: 'r',
};

export const sampleWithPartialData: IAnneeAcademique = {
  id: 31198,
  libelleAnneeAcademique: "aujourd'hui",
  anneeAc: 2041,
  separateur: 'p',
  anneeCouranteYN: false,
};

export const sampleWithFullData: IAnneeAcademique = {
  id: 7506,
  libelleAnneeAcademique: 'lentement secours vu que',
  anneeAc: 2058,
  separateur: 't',
  anneeCouranteYN: false,
};

export const sampleWithNewData: NewAnneeAcademique = {
  libelleAnneeAcademique: 'céans',
  anneeAc: 2034,
  separateur: 'a',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
