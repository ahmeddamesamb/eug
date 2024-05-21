import dayjs from 'dayjs/esm';

import { IMinistere, NewMinistere } from './ministere.model';

export const sampleWithRequiredData: IMinistere = {
  id: 6369,
};

export const sampleWithPartialData: IMinistere = {
  id: 7793,
  dateDebut: dayjs('2024-05-17'),
  dateFin: dayjs('2024-05-17'),
};

export const sampleWithFullData: IMinistere = {
  id: 7236,
  nomMinistere: 'since',
  sigleMinistere: 'soap eyeliner trifling',
  dateDebut: dayjs('2024-05-17'),
  dateFin: dayjs('2024-05-17'),
  enCours: 25678,
};

export const sampleWithNewData: NewMinistere = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
