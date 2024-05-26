import { IMention, NewMention } from './mention.model';

export const sampleWithRequiredData: IMention = {
  id: 28946,
  libelleMention: 'ping sandbar',
};

export const sampleWithPartialData: IMention = {
  id: 15638,
  libelleMention: 'sock duh',
};

export const sampleWithFullData: IMention = {
  id: 15208,
  libelleMention: 'opposite invitation aha',
};

export const sampleWithNewData: NewMention = {
  libelleMention: 'phew intestine after',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
