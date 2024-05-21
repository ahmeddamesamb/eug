import { IMention, NewMention } from './mention.model';

export const sampleWithRequiredData: IMention = {
  id: 28946,
};

export const sampleWithPartialData: IMention = {
  id: 14870,
};

export const sampleWithFullData: IMention = {
  id: 1814,
  libelleMention: 'abaft',
};

export const sampleWithNewData: NewMention = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
