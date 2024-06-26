import dayjs from 'dayjs/esm';

import { IPaiementFrais, NewPaiementFrais } from './paiement-frais.model';

export const sampleWithRequiredData: IPaiementFrais = {
  id: 8476,
  datePaiement: dayjs('2024-06-24'),
};

export const sampleWithPartialData: IPaiementFrais = {
  id: 11814,
  datePaiement: dayjs('2024-06-25'),
  obligatoireYN: false,
  forclosYN: true,
  paimentDelaiYN: true,
};

export const sampleWithFullData: IPaiementFrais = {
  id: 2449,
  datePaiement: dayjs('2024-06-25'),
  obligatoireYN: false,
  echeancePayeeYN: false,
  emailUser: 'bientôt lors',
  dateForclos: dayjs('2024-06-25'),
  forclosYN: true,
  paimentDelaiYN: false,
};

export const sampleWithNewData: NewPaiementFrais = {
  datePaiement: dayjs('2024-06-25'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
