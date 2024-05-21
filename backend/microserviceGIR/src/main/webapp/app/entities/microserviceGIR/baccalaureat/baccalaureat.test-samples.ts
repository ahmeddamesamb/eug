import dayjs from 'dayjs/esm';

import { IBaccalaureat, NewBaccalaureat } from './baccalaureat.model';

export const sampleWithRequiredData: IBaccalaureat = {
  id: 8247,
  anneeBac: dayjs('2024-05-18'),
  numeroTable: 29841,
  mentionBac: 'regurgitate',
  moyenneBac: 16791.52,
};

export const sampleWithPartialData: IBaccalaureat = {
  id: 15844,
  anneeBac: dayjs('2024-05-18'),
  numeroTable: 21385,
  mentionBac: 'total slimy through',
  moyenneSelectionBac: 27058.53,
  moyenneBac: 6562.56,
};

export const sampleWithFullData: IBaccalaureat = {
  id: 14517,
  origineScolaire: 'procurement',
  anneeBac: dayjs('2024-05-17'),
  numeroTable: 7799,
  natureBac: 'out money caution',
  mentionBac: 'blah',
  moyenneSelectionBac: 3896.28,
  moyenneBac: 8507.64,
};

export const sampleWithNewData: NewBaccalaureat = {
  anneeBac: dayjs('2024-05-17'),
  numeroTable: 21517,
  mentionBac: 'absolute duh phew',
  moyenneBac: 29698.22,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
