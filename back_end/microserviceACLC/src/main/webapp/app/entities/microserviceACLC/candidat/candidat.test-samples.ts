import dayjs from 'dayjs/esm';

import { ICandidat, NewCandidat } from './candidat.model';

export const sampleWithRequiredData: ICandidat = {
  id: 5697,
};

export const sampleWithPartialData: ICandidat = {
  id: 6912,
  nomCanditat: 'overlie uh-huh space',
  dateNaissanceCandidat: dayjs('2024-05-24'),
  emailCandidat: 'desecrate carriage thrift',
};

export const sampleWithFullData: ICandidat = {
  id: 13580,
  nomCanditat: 'since institutionalise',
  prenomCandidat: 'yummy incidentally negligible',
  dateNaissanceCandidat: dayjs('2024-05-24'),
  emailCandidat: 'seldom consequently',
};

export const sampleWithNewData: NewCandidat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
