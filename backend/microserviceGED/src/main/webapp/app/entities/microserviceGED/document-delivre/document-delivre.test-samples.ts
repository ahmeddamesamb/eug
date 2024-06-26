import dayjs from 'dayjs/esm';

import { IDocumentDelivre, NewDocumentDelivre } from './document-delivre.model';

export const sampleWithRequiredData: IDocumentDelivre = {
  id: 25176,
};

export const sampleWithPartialData: IDocumentDelivre = {
  id: 23839,
  libelleDoc: 'ouf arrière vu que',
};

export const sampleWithFullData: IDocumentDelivre = {
  id: 2590,
  libelleDoc: 'menacer tandis que',
  anneeDoc: dayjs('2024-06-25T12:27'),
  dateEnregistrement: dayjs('2024-06-25T06:13'),
};

export const sampleWithNewData: NewDocumentDelivre = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
