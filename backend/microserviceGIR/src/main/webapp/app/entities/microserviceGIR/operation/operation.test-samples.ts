import dayjs from 'dayjs/esm';

import { IOperation, NewOperation } from './operation.model';

export const sampleWithRequiredData: IOperation = {
  id: 19409,
};

export const sampleWithPartialData: IOperation = {
  id: 22714,
  emailUser: 'plaisanter au lieu de',
  detailOperation: '../fake-data/blob/hipster.txt',
  infoSysteme: 'tremper foule fourbe',
};

export const sampleWithFullData: IOperation = {
  id: 2168,
  dateExecution: dayjs('2024-06-25T00:58'),
  emailUser: 'durant probablement',
  detailOperation: '../fake-data/blob/hipster.txt',
  infoSysteme: 'coupable frayer',
};

export const sampleWithNewData: NewOperation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
