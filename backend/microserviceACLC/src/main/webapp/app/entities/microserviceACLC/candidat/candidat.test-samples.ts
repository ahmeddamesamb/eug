import dayjs from 'dayjs/esm';

import { ICandidat, NewCandidat } from './candidat.model';

export const sampleWithRequiredData: ICandidat = {
  id: 5697,
};

export const sampleWithPartialData: ICandidat = {
  id: 6912,
  nom: 'intégrer ronron contrarier',
  dateNaissance: dayjs('2024-06-25'),
  email: 'Victor_Benoit65@hotmail.fr',
};

export const sampleWithFullData: ICandidat = {
  id: 7680,
  nom: 'loin de à force de',
  prenom: 'en bas de recueillir',
  dateNaissance: dayjs('2024-06-25'),
  email: 'Barbe_Remy99@yahoo.fr',
};

export const sampleWithNewData: NewCandidat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
