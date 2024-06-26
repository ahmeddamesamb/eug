import { IEnseignement, NewEnseignement } from './enseignement.model';

export const sampleWithRequiredData: IEnseignement = {
  id: 26313,
};

export const sampleWithPartialData: IEnseignement = {
  id: 15197,
  volumeHoraire: 5973.42,
  groupeYN: false,
};

export const sampleWithFullData: IEnseignement = {
  id: 23269,
  libelleEnseignements: 'de crainte que',
  volumeHoraire: 14706.56,
  nombreInscrits: 8004,
  groupeYN: true,
};

export const sampleWithNewData: NewEnseignement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
