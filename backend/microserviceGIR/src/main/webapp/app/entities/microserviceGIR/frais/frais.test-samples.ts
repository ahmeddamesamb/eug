import dayjs from 'dayjs/esm';

import { IFrais, NewFrais } from './frais.model';

export const sampleWithRequiredData: IFrais = {
  id: 3695,
  valeurFrais: 20974.15,
  dateApplication: dayjs('2024-06-25'),
  estEnApplicationYN: true,
};

export const sampleWithPartialData: IFrais = {
  id: 3551,
  valeurFrais: 4208.09,
  fraisPourExonererYN: true,
  dia: 7456.18,
  dip: 24199.37,
  fraisPrivee: 13467.91,
  dateApplication: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-25'),
  estEnApplicationYN: false,
  actifYN: false,
};

export const sampleWithFullData: IFrais = {
  id: 18675,
  valeurFrais: 30492.68,
  descriptionFrais: 'tchou tchouu parlementaire',
  fraisPourAssimileYN: true,
  fraisPourExonererYN: true,
  dia: 7468.66,
  dip: 8583.22,
  fraisPrivee: 8992.4,
  dateApplication: dayjs('2024-06-25'),
  dateFin: dayjs('2024-06-24'),
  estEnApplicationYN: true,
  actifYN: false,
};

export const sampleWithNewData: NewFrais = {
  valeurFrais: 20439.4,
  dateApplication: dayjs('2024-06-25'),
  estEnApplicationYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
