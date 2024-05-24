import dayjs from 'dayjs/esm';

import { IPaiementFormationPrivee, NewPaiementFormationPrivee } from './paiement-formation-privee.model';

export const sampleWithRequiredData: IPaiementFormationPrivee = {
  id: 27307,
};

export const sampleWithPartialData: IPaiementFormationPrivee = {
  id: 8926,
  moisPaiement: 'ugh besides',
  anneePaiement: 'ah',
  payerMensualiteYN: 2411,
  emailUser: 'overconfidently',
};

export const sampleWithFullData: IPaiementFormationPrivee = {
  id: 22201,
  datePaiement: dayjs('2024-05-24T16:21'),
  moisPaiement: 'bail politely globalise',
  anneePaiement: 'certainly cull',
  payerMensualiteYN: 9640,
  payerDelaisYN: 9466,
  emailUser: 'seriously woefully ground',
};

export const sampleWithNewData: NewPaiementFormationPrivee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
