import { ICycle, NewCycle } from './cycle.model';

export const sampleWithRequiredData: ICycle = {
  id: 22967,
  libelleCycle: 'entre-temps miam juriste',
};

export const sampleWithPartialData: ICycle = {
  id: 32185,
  libelleCycle: 'neutre',
};

export const sampleWithFullData: ICycle = {
  id: 15665,
  libelleCycle: 'de manière à ce que',
};

export const sampleWithNewData: NewCycle = {
  libelleCycle: 'au-dehors téléphoner secours',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
