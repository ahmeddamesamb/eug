import dayjs from 'dayjs/esm';

import { IPaiementFormationPrivee, NewPaiementFormationPrivee } from './paiement-formation-privee.model';

export const sampleWithRequiredData: IPaiementFormationPrivee = {
  id: 28489,
};

export const sampleWithPartialData: IPaiementFormationPrivee = {
  id: 28394,
  anneePaiement: 'caper ruler',
  payerMensualiteYN: 27937,
  emailUser: 'or',
};

export const sampleWithFullData: IPaiementFormationPrivee = {
  id: 9294,
  datePaiement: dayjs('2024-05-18T14:04'),
  moisPaiement: 'overconfidently',
  anneePaiement: 'expend until off',
  payerMensualiteYN: 16960,
  emailUser: 'abnormally fumble',
};

export const sampleWithNewData: NewPaiementFormationPrivee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
