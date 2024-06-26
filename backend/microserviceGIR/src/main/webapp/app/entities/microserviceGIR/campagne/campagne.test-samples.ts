import dayjs from 'dayjs/esm';

import { ICampagne, NewCampagne } from './campagne.model';

export const sampleWithRequiredData: ICampagne = {
  id: 27628,
};

export const sampleWithPartialData: ICampagne = {
  id: 1484,
  libelleCampagne: 'toujours oups',
  dateFin: dayjs('2024-06-25'),
  libelleAbrege: 'cocorico',
  actifYN: true,
};

export const sampleWithFullData: ICampagne = {
  id: 17976,
  libelleCampagne: 'diplomate aussitôt que',
  dateDebut: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-25'),
  libelleAbrege: 'rédaction au moyen de',
  actifYN: true,
};

export const sampleWithNewData: NewCampagne = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
