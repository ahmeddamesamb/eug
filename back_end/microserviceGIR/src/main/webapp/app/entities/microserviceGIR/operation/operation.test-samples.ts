import dayjs from 'dayjs/esm';

import { IOperation, NewOperation } from './operation.model';

export const sampleWithRequiredData: IOperation = {
  id: 19409,
};

export const sampleWithPartialData: IOperation = {
  id: 22714,
  emailUser: 'supply notwithstanding',
  detailOperation: '../fake-data/blob/hipster.txt',
  infoSysteme: 'undervalue weeder giant',
};

export const sampleWithFullData: IOperation = {
  id: 2168,
  dateExecution: dayjs('2024-05-23T22:05'),
  emailUser: 'amidst willfully',
  detailOperation: '../fake-data/blob/hipster.txt',
  infoSysteme: 'daring tremble',
};

export const sampleWithNewData: NewOperation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
