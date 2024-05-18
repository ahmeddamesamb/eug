import { ISpecialite, NewSpecialite } from './specialite.model';

export const sampleWithRequiredData: ISpecialite = {
  id: 12158,
};

export const sampleWithPartialData: ISpecialite = {
  id: 8194,
  sigleSpecialites: 'proud',
  specialitesPayanteYN: 8744,
};

export const sampleWithFullData: ISpecialite = {
  id: 25701,
  nomSpecialites: 'dog',
  sigleSpecialites: 'boohoo',
  specialiteParticulierYN: 17535,
  specialitesPayanteYN: 18250,
};

export const sampleWithNewData: NewSpecialite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
