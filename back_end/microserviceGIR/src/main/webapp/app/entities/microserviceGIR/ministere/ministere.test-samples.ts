import dayjs from 'dayjs/esm';

import { IMinistere, NewMinistere } from './ministere.model';

export const sampleWithRequiredData: IMinistere = {
  id: 6369,
  nomMinistere: 'ick lest madly',
  dateDebut: dayjs('2024-05-24'),
  enCoursYN: 6591,
};

export const sampleWithPartialData: IMinistere = {
  id: 8152,
  nomMinistere: 'easy sparkling',
  dateDebut: dayjs('2024-05-24'),
  enCoursYN: 24039,
};

export const sampleWithFullData: IMinistere = {
  id: 25316,
  nomMinistere: 'squatter presell indeed',
  sigleMinistere: 'model',
  dateDebut: dayjs('2024-05-24'),
  dateFin: dayjs('2024-05-24'),
  enCoursYN: 1176,
};

export const sampleWithNewData: NewMinistere = {
  nomMinistere: 'a beatboxer prudent',
  dateDebut: dayjs('2024-05-24'),
  enCoursYN: 15850,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
