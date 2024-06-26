import dayjs from 'dayjs/esm';

import { IPaiementFormationPrivee, NewPaiementFormationPrivee } from './paiement-formation-privee.model';

export const sampleWithRequiredData: IPaiementFormationPrivee = {
  id: 27307,
};

export const sampleWithPartialData: IPaiementFormationPrivee = {
  id: 8926,
  moisPaiement: 'miaou sous',
  anneePaiement: 'cot cot',
  payerMensualiteYN: true,
  emailUser: 'désormais',
};

export const sampleWithFullData: IPaiementFormationPrivee = {
  id: 22201,
  datePaiement: dayjs('2024-06-25T19:13'),
  moisPaiement: 'porter entre-temps manifester',
  anneePaiement: 'mal remplir',
  payerMensualiteYN: true,
  payerDelaisYN: true,
  emailUser: 'ailleurs sans doute achever',
};

export const sampleWithNewData: NewPaiementFormationPrivee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
