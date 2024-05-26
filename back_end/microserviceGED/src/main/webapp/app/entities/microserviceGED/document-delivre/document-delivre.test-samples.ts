import dayjs from 'dayjs/esm';

import { IDocumentDelivre, NewDocumentDelivre } from './document-delivre.model';

export const sampleWithRequiredData: IDocumentDelivre = {
  id: 25176,
};

export const sampleWithPartialData: IDocumentDelivre = {
  id: 23839,
  libelleDoc: 'gee shrilly how',
};

export const sampleWithFullData: IDocumentDelivre = {
  id: 2590,
  libelleDoc: 'solicit why',
  anneeDoc: dayjs('2024-05-24T09:36'),
  dateEnregistrement: dayjs('2024-05-24T03:21'),
};

export const sampleWithNewData: NewDocumentDelivre = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
