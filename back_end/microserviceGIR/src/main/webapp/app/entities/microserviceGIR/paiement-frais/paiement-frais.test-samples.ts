import dayjs from 'dayjs/esm';

import { IPaiementFrais, NewPaiementFrais } from './paiement-frais.model';

export const sampleWithRequiredData: IPaiementFrais = {
  id: 14507,
  datePaiement: dayjs('2024-05-24'),
};

export const sampleWithPartialData: IPaiementFrais = {
  id: 277,
  datePaiement: dayjs('2024-05-24'),
  echeancePayeeYN: 11814,
};

export const sampleWithFullData: IPaiementFrais = {
  id: 1214,
  datePaiement: dayjs('2024-05-23'),
  obligatoireYN: 667,
  echeancePayeeYN: 13081,
  emailUser: 'loose',
  dateForclos: dayjs('2024-05-23'),
  forclosYN: 1071,
};

export const sampleWithNewData: NewPaiementFrais = {
  datePaiement: dayjs('2024-05-24'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
