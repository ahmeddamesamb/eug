import dayjs from 'dayjs/esm';

import { IMinistere, NewMinistere } from './ministere.model';

export const sampleWithRequiredData: IMinistere = {
  id: 31511,
  nomMinistere: 'clac pour que si',
  dateDebut: dayjs('2024-06-25'),
  enCoursYN: false,
  actifYN: false,
};

export const sampleWithPartialData: IMinistere = {
  id: 28134,
  nomMinistere: 'par rapport à',
  sigleMinistere: 'sédentaire paf clac',
  dateDebut: dayjs('2024-06-25'),
  enCoursYN: true,
  actifYN: false,
};

export const sampleWithFullData: IMinistere = {
  id: 22725,
  nomMinistere: 'dès que',
  sigleMinistere: 'psitt',
  dateDebut: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-25'),
  enCoursYN: true,
  actifYN: true,
};

export const sampleWithNewData: NewMinistere = {
  nomMinistere: 'tôt à la faveur de',
  dateDebut: dayjs('2024-06-25'),
  enCoursYN: false,
  actifYN: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
