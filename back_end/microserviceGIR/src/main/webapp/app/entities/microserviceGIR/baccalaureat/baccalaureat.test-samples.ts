import dayjs from 'dayjs/esm';

import { IBaccalaureat, NewBaccalaureat } from './baccalaureat.model';

export const sampleWithRequiredData: IBaccalaureat = {
  id: 8247,
};

export const sampleWithPartialData: IBaccalaureat = {
  id: 9273,
  origineScolaire: 'coaxingly previous',
  numeroTable: 28464,
  natureBac: 'slimy through minor',
  moyenneSelectionBac: 9247.11,
  moyenneBac: 13893.24,
};

export const sampleWithFullData: IBaccalaureat = {
  id: 9269,
  origineScolaire: 'intent synthesis edify',
  anneeBac: dayjs('2024-05-23'),
  numeroTable: 3686,
  natureBac: 'rebrand',
  mentionBac: 'vice',
  moyenneSelectionBac: 20045.32,
  moyenneBac: 22540.07,
};

export const sampleWithNewData: NewBaccalaureat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
