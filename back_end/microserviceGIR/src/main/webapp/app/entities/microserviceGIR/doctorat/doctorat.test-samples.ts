import dayjs from 'dayjs/esm';

import { IDoctorat, NewDoctorat } from './doctorat.model';

export const sampleWithRequiredData: IDoctorat = {
  id: 15665,
  sujet: 'delimit',
};

export const sampleWithPartialData: IDoctorat = {
  id: 8837,
  sujet: 'proper bloop',
  anneeInscriptionDoctorat: dayjs('2024-05-24'),
  encadreurId: 32717,
  laboratoirId: 22296,
};

export const sampleWithFullData: IDoctorat = {
  id: 2571,
  sujet: 'closed yieldingly whoever',
  anneeInscriptionDoctorat: dayjs('2024-05-24'),
  encadreurId: 17256,
  laboratoirId: 5235,
};

export const sampleWithNewData: NewDoctorat = {
  sujet: 'extremely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
