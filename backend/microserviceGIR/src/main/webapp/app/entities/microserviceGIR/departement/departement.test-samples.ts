import { IDepartement, NewDepartement } from './departement.model';

export const sampleWithRequiredData: IDepartement = {
  id: 7556,
  nomDepatement: 'communauté étudiante clientèle',
};

export const sampleWithPartialData: IDepartement = {
  id: 10533,
  nomDepatement: 'vétuste trop',
};

export const sampleWithFullData: IDepartement = {
  id: 14591,
  nomDepatement: 'direction au-dessus de',
  actifYN: true,
};

export const sampleWithNewData: NewDepartement = {
  nomDepatement: 'turquoise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
