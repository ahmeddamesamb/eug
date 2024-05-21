import dayjs from 'dayjs/esm';

import { IFrais, NewFrais } from './frais.model';

export const sampleWithRequiredData: IFrais = {
  id: 11055,
};

export const sampleWithPartialData: IFrais = {
  id: 11781,
  valeurFrais: 15830.49,
  cycle: 'LICENCE',
  dipPrivee: 4208.09,
  dateApplication: dayjs('2024-05-18'),
  dateFin: dayjs('2024-05-18'),
  estEnApplicationYN: 24200,
};

export const sampleWithFullData: IFrais = {
  id: 13468,
  valeurFrais: 7848.09,
  descriptionFrais: 'second instantly aha',
  fraispourAssimileYN: 7468,
  cycle: 'LICENCE',
  dia: 8992.4,
  dip: 7202.13,
  dipPrivee: 32025.08,
  dateApplication: dayjs('2024-05-18'),
  dateFin: dayjs('2024-05-17'),
  estEnApplicationYN: 20440,
};

export const sampleWithNewData: NewFrais = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
