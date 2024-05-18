import dayjs from 'dayjs/esm';

import { IPaiementFrais, NewPaiementFrais } from './paiement-frais.model';

export const sampleWithRequiredData: IPaiementFrais = {
  id: 17610,
};

export const sampleWithPartialData: IPaiementFrais = {
  id: 3516,
  obligatoireYN: 29050,
  echeancePayeeYN: 22810,
};

export const sampleWithFullData: IPaiementFrais = {
  id: 31964,
  datePaiement: dayjs('2024-05-18'),
  obligatoireYN: 12477,
  echeancePayeeYN: 11814,
  emailUser: 'ick',
};

export const sampleWithNewData: NewPaiementFrais = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
