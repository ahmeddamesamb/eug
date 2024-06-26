import dayjs from 'dayjs/esm';

import { IRapport, NewRapport } from './rapport.model';

export const sampleWithRequiredData: IRapport = {
  id: 11582,
};

export const sampleWithPartialData: IRapport = {
  id: 21945,
  libelleRapport: 'en decà de à bas de',
  contenuRapport: 'dynamique touriste admirablement',
  dateRedaction: dayjs('2024-06-25T09:22'),
};

export const sampleWithFullData: IRapport = {
  id: 15048,
  libelleRapport: 'pourpre y',
  descriptionRapport: 'selon blême minuscule',
  contenuRapport: 'responsable',
  dateRedaction: dayjs('2024-06-24T23:56'),
};

export const sampleWithNewData: NewRapport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
