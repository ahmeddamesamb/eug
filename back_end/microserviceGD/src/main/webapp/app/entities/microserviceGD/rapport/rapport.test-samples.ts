import dayjs from 'dayjs/esm';

import { IRapport, NewRapport } from './rapport.model';

export const sampleWithRequiredData: IRapport = {
  id: 11582,
};

export const sampleWithPartialData: IRapport = {
  id: 21945,
  libelleRapport: 'than concerning',
  contenuRapport: 'elated mini absentmindedly',
  dateRedaction: dayjs('2024-05-24T06:31'),
};

export const sampleWithFullData: IRapport = {
  id: 15048,
  libelleRapport: 'regal usually',
  descriptionRapport: 'beneath busy ordinary',
  contenuRapport: 'chrysalis',
  dateRedaction: dayjs('2024-05-23T21:04'),
};

export const sampleWithNewData: NewRapport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
