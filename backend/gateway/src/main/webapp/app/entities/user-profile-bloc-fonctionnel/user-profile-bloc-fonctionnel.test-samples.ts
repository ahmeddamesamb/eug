import dayjs from 'dayjs/esm';

import { IUserProfileBlocFonctionnel, NewUserProfileBlocFonctionnel } from './user-profile-bloc-fonctionnel.model';

export const sampleWithRequiredData: IUserProfileBlocFonctionnel = {
  id: 25453,
  date: dayjs('2024-06-25'),
  enCoursYN: false,
};

export const sampleWithPartialData: IUserProfileBlocFonctionnel = {
  id: 13188,
  date: dayjs('2024-06-25'),
  enCoursYN: false,
};

export const sampleWithFullData: IUserProfileBlocFonctionnel = {
  id: 10032,
  date: dayjs('2024-06-25'),
  enCoursYN: false,
};

export const sampleWithNewData: NewUserProfileBlocFonctionnel = {
  date: dayjs('2024-06-25'),
  enCoursYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
