import dayjs from 'dayjs/esm';

import { IPlanning, NewPlanning } from './planning.model';

export const sampleWithRequiredData: IPlanning = {
  id: 27244,
  dateDebut: dayjs('2024-05-17T18:20'),
  dateFin: dayjs('2024-05-18T07:06'),
};

export const sampleWithPartialData: IPlanning = {
  id: 31865,
  dateDebut: dayjs('2024-05-18T00:32'),
  dateFin: dayjs('2024-05-17T19:02'),
};

export const sampleWithFullData: IPlanning = {
  id: 24243,
  dateDebut: dayjs('2024-05-18T15:19'),
  dateFin: dayjs('2024-05-18T06:10'),
};

export const sampleWithNewData: NewPlanning = {
  dateDebut: dayjs('2024-05-17T22:48'),
  dateFin: dayjs('2024-05-18T03:06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
