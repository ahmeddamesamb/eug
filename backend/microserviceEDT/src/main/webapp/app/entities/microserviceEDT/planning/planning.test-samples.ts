import dayjs from 'dayjs/esm';

import { IPlanning, NewPlanning } from './planning.model';

export const sampleWithRequiredData: IPlanning = {
  id: 27244,
};

export const sampleWithPartialData: IPlanning = {
  id: 31865,
  dateFin: dayjs('2024-06-25T05:39'),
};

export const sampleWithFullData: IPlanning = {
  id: 28374,
  dateDebut: dayjs('2024-06-25T03:10'),
  dateFin: dayjs('2024-06-25T20:26'),
};

export const sampleWithNewData: NewPlanning = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
