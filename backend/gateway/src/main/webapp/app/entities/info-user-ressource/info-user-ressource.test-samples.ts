import dayjs from 'dayjs/esm';

import { IInfoUserRessource, NewInfoUserRessource } from './info-user-ressource.model';

export const sampleWithRequiredData: IInfoUserRessource = {
  id: 8735,
  dateAjout: dayjs('2024-06-25'),
  enCoursYN: true,
};

export const sampleWithPartialData: IInfoUserRessource = {
  id: 25406,
  dateAjout: dayjs('2024-06-25'),
  enCoursYN: false,
};

export const sampleWithFullData: IInfoUserRessource = {
  id: 21919,
  dateAjout: dayjs('2024-06-25'),
  enCoursYN: true,
};

export const sampleWithNewData: NewInfoUserRessource = {
  dateAjout: dayjs('2024-06-25'),
  enCoursYN: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
