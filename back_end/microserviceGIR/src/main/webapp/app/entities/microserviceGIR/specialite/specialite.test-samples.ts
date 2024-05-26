import { ISpecialite, NewSpecialite } from './specialite.model';

export const sampleWithRequiredData: ISpecialite = {
  id: 12158,
  nomSpecialites: 'whoever seed grip',
  sigleSpecialites: 'or',
  specialitesPayanteYN: 6053,
};

export const sampleWithPartialData: ISpecialite = {
  id: 18250,
  nomSpecialites: 'memorial decisive',
  sigleSpecialites: 'marvelous',
  specialitesPayanteYN: 2811,
};

export const sampleWithFullData: ISpecialite = {
  id: 407,
  nomSpecialites: 'dub ride at',
  sigleSpecialites: 'joshingly gee aw',
  specialiteParticulierYN: 27355,
  specialitesPayanteYN: 9401,
};

export const sampleWithNewData: NewSpecialite = {
  nomSpecialites: 'drift acquaint respectful',
  sigleSpecialites: 'cleverly',
  specialitesPayanteYN: 4577,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
