import dayjs from 'dayjs/esm';

import { IDoctorat, NewDoctorat } from './doctorat.model';

export const sampleWithRequiredData: IDoctorat = {
  id: 15665,
};

export const sampleWithPartialData: IDoctorat = {
  id: 13712,
  sujet: 'methane',
};

export const sampleWithFullData: IDoctorat = {
  id: 20212,
  sujet: 'rationale um till',
  anneeInscriptionDoctorat: dayjs('2024-05-18'),
  encadreurId: 19354,
  laboratoirId: 4068,
};

export const sampleWithNewData: NewDoctorat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
