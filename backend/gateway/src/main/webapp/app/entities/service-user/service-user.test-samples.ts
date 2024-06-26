import dayjs from 'dayjs/esm';

import { IServiceUser, NewServiceUser } from './service-user.model';

export const sampleWithRequiredData: IServiceUser = {
  id: 7874,
  nom: 'areu areu',
  dateAjout: dayjs('2024-06-25'),
  actifYN: true,
};

export const sampleWithPartialData: IServiceUser = {
  id: 16554,
  nom: 'que dans la mesure où toc',
  dateAjout: dayjs('2024-06-25'),
  actifYN: false,
};

export const sampleWithFullData: IServiceUser = {
  id: 21083,
  nom: 'dring dès que pourvu que',
  dateAjout: dayjs('2024-06-25'),
  actifYN: false,
};

export const sampleWithNewData: NewServiceUser = {
  nom: 'pendant blablabla',
  dateAjout: dayjs('2024-06-25'),
  actifYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
