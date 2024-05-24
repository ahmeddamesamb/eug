import dayjs from 'dayjs/esm';

import { ICampagne, NewCampagne } from './campagne.model';

export const sampleWithRequiredData: ICampagne = {
  id: 26828,
};

export const sampleWithPartialData: ICampagne = {
  id: 3669,
  dateDebut: dayjs('2024-05-24'),
  libelleAbrege: 'yum',
};

export const sampleWithFullData: ICampagne = {
  id: 23362,
  libelleCampagne: 'hydroxyl allot red',
  dateDebut: dayjs('2024-05-24'),
  dateFin: dayjs('2024-05-24'),
  libelleAbrege: 'tensely',
};

export const sampleWithNewData: NewCampagne = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
