import dayjs from 'dayjs/esm';

import { IFrais, NewFrais } from './frais.model';

export const sampleWithRequiredData: IFrais = {
  id: 11055,
  valeurFrais: 3695.04,
  cycle: 'MASTER',
  dateApplication: dayjs('2024-05-24'),
  estEnApplicationYN: 11108,
};

export const sampleWithPartialData: IFrais = {
  id: 11781,
  valeurFrais: 15830.49,
  cycle: 'LICENCE',
  dia: 4208.09,
  dip: 7541.96,
  dipPrivee: 7456.18,
  dateApplication: dayjs('2024-05-24'),
  dateFin: dayjs('2024-05-24'),
  estEnApplicationYN: 7848,
};

export const sampleWithFullData: IFrais = {
  id: 24597,
  valeurFrais: 29224.35,
  descriptionFrais: 'implement vacation barring',
  fraisPourAssimileYN: 8992,
  cycle: 'LICENCE',
  dia: 32025.08,
  dip: 6558.31,
  dipPrivee: 30167.06,
  dateApplication: dayjs('2024-05-24'),
  dateFin: dayjs('2024-05-23'),
  estEnApplicationYN: 26535,
};

export const sampleWithNewData: NewFrais = {
  valeurFrais: 6690.45,
  cycle: 'MASTER',
  dateApplication: dayjs('2024-05-24'),
  estEnApplicationYN: 30072,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
