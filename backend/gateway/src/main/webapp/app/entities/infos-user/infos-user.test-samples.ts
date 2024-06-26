import dayjs from 'dayjs/esm';

import { IInfosUser, NewInfosUser } from './infos-user.model';

export const sampleWithRequiredData: IInfosUser = {
  id: 10099,
  dateAjout: dayjs('2024-06-25'),
  actifYN: true,
};

export const sampleWithPartialData: IInfosUser = {
  id: 25994,
  dateAjout: dayjs('2024-06-25'),
  actifYN: true,
};

export const sampleWithFullData: IInfosUser = {
  id: 15112,
  dateAjout: dayjs('2024-06-25'),
  actifYN: false,
};

export const sampleWithNewData: NewInfosUser = {
  dateAjout: dayjs('2024-06-25'),
  actifYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
