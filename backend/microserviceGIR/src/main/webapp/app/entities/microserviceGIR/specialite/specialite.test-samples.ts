import { ISpecialite, NewSpecialite } from './specialite.model';

export const sampleWithRequiredData: ISpecialite = {
  id: 26419,
  nomSpecialites: 'mature',
  sigleSpecialites: 'concurrence de façon à ce que',
  specialitesPayanteYN: false,
};

export const sampleWithPartialData: ISpecialite = {
  id: 2092,
  nomSpecialites: 'enrichir nager dégager',
  sigleSpecialites: 'cadre contrarier à partir de',
  specialiteParticulierYN: false,
  specialitesPayanteYN: false,
  actifYN: false,
};

export const sampleWithFullData: ISpecialite = {
  id: 32641,
  nomSpecialites: 'concernant vouh',
  sigleSpecialites: 'd’autant que',
  specialiteParticulierYN: false,
  specialitesPayanteYN: true,
  actifYN: false,
};

export const sampleWithNewData: NewSpecialite = {
  nomSpecialites: "incognito spécialiste d'entre",
  sigleSpecialites: 'qualifier toc vraiment',
  specialitesPayanteYN: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
