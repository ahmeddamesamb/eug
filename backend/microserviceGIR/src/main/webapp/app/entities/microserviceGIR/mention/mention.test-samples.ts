import { IMention, NewMention } from './mention.model';

export const sampleWithRequiredData: IMention = {
  id: 19127,
  libelleMention: "critiquer à l'exception de",
};

export const sampleWithPartialData: IMention = {
  id: 30813,
  libelleMention: 'actionnaire',
};

export const sampleWithFullData: IMention = {
  id: 4063,
  libelleMention: 'partenaire du fait que',
  actifYN: true,
};

export const sampleWithNewData: NewMention = {
  libelleMention: 'insuffisamment',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
