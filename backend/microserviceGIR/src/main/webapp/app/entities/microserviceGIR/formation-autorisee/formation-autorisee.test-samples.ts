import dayjs from 'dayjs/esm';

import { IFormationAutorisee, NewFormationAutorisee } from './formation-autorisee.model';

export const sampleWithRequiredData: IFormationAutorisee = {
  id: 25482,
};

export const sampleWithPartialData: IFormationAutorisee = {
  id: 25106,
  dateDebut: dayjs('2024-06-25'),
  enCoursYN: true,
};

export const sampleWithFullData: IFormationAutorisee = {
  id: 13082,
  dateDebut: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-25'),
  enCoursYN: true,
  actifYN: true,
};

export const sampleWithNewData: NewFormationAutorisee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
