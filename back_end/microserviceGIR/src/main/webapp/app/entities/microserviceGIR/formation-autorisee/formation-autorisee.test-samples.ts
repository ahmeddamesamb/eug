import dayjs from 'dayjs/esm';

import { IFormationAutorisee, NewFormationAutorisee } from './formation-autorisee.model';

export const sampleWithRequiredData: IFormationAutorisee = {
  id: 15998,
};

export const sampleWithPartialData: IFormationAutorisee = {
  id: 11767,
  dateFin: dayjs('2024-05-23'),
};

export const sampleWithFullData: IFormationAutorisee = {
  id: 25106,
  dateDebut: dayjs('2024-05-24'),
  dateFin: dayjs('2024-05-24'),
  enCoursYN: 13082,
};

export const sampleWithNewData: NewFormationAutorisee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
