import dayjs from 'dayjs/esm';

import { IDoctorat, NewDoctorat } from './doctorat.model';

export const sampleWithRequiredData: IDoctorat = {
  id: 15665,
  sujet: 'renoncer',
};

export const sampleWithPartialData: IDoctorat = {
  id: 8837,
  sujet: 'pauvre imaginer',
  anneeInscriptionDoctorat: dayjs('2024-06-25'),
  encadreurId: 32717,
  laboratoirId: 22296,
};

export const sampleWithFullData: IDoctorat = {
  id: 2571,
  sujet: 'calme vraisemblablement tant que',
  anneeInscriptionDoctorat: dayjs('2024-06-25'),
  encadreurId: 17256,
  laboratoirId: 5235,
};

export const sampleWithNewData: NewDoctorat = {
  sujet: 'complètement',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
