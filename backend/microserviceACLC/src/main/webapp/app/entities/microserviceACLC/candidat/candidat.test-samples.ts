import dayjs from 'dayjs/esm';

import { ICandidat, NewCandidat } from './candidat.model';

export const sampleWithRequiredData: ICandidat = {
  id: 5697,
};

export const sampleWithPartialData: ICandidat = {
  id: 6912,
  nom: 'overlie uh-huh space',
  dateNaissance: dayjs('2024-05-18'),
  email: 'Trisha_Schoen65@hotmail.com',
};

export const sampleWithFullData: ICandidat = {
  id: 7680,
  nom: 'until except',
  prenom: 'since institutionalise',
  dateNaissance: dayjs('2024-05-17'),
  email: 'Edgar_Will99@yahoo.com',
};

export const sampleWithNewData: NewCandidat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
