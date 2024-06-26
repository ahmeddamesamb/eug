import dayjs from 'dayjs/esm';

import { IBaccalaureat, NewBaccalaureat } from './baccalaureat.model';

export const sampleWithRequiredData: IBaccalaureat = {
  id: 10314,
};

export const sampleWithPartialData: IBaccalaureat = {
  id: 23271,
  anneeBac: dayjs('2024-06-25'),
  numeroTable: 22279,
  mentionBac: 'hôte cyan',
  moyenneSelectionBac: 7018.11,
  moyenneBac: 29705.69,
};

export const sampleWithFullData: IBaccalaureat = {
  id: 29316,
  origineScolaire: 'police tant efficace',
  anneeBac: dayjs('2024-06-25'),
  numeroTable: 31256,
  natureBac: 'échouer actionnaire',
  mentionBac: 'différer entièrement pourpre',
  moyenneSelectionBac: 24316.4,
  moyenneBac: 27682.94,
  actifYN: true,
};

export const sampleWithNewData: NewBaccalaureat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
