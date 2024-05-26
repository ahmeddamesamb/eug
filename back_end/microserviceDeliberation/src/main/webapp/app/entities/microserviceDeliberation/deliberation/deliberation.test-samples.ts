import { IDeliberation, NewDeliberation } from './deliberation.model';

export const sampleWithRequiredData: IDeliberation = {
  id: 26989,
};

export const sampleWithPartialData: IDeliberation = {
  id: 14517,
  estValideeYN: 23425,
  pvDeliberation: '../fake-data/blob/hipster.png',
  pvDeliberationContentType: 'unknown',
};

export const sampleWithFullData: IDeliberation = {
  id: 25769,
  estValideeYN: 9588,
  pvDeliberation: '../fake-data/blob/hipster.png',
  pvDeliberationContentType: 'unknown',
};

export const sampleWithNewData: NewDeliberation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
