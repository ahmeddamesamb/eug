import dayjs from 'dayjs/esm';

import { IUserProfile, NewUserProfile } from './user-profile.model';

export const sampleWithRequiredData: IUserProfile = {
  id: 26752,
  dateDebut: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-25'),
  enCoursYN: true,
};

export const sampleWithPartialData: IUserProfile = {
  id: 13956,
  dateDebut: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-25'),
  enCoursYN: true,
};

export const sampleWithFullData: IUserProfile = {
  id: 15681,
  dateDebut: dayjs('2024-06-24'),
  dateFin: dayjs('2024-06-25'),
  enCoursYN: true,
};

export const sampleWithNewData: NewUserProfile = {
  dateDebut: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-25'),
  enCoursYN: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
