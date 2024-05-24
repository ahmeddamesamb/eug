import { IZone, NewZone } from './zone.model';

export const sampleWithRequiredData: IZone = {
  id: 27060,
  libelleZone: 'boohoo till',
};

export const sampleWithPartialData: IZone = {
  id: 391,
  libelleZone: 'glaring',
};

export const sampleWithFullData: IZone = {
  id: 23836,
  libelleZone: 'carpet across while',
};

export const sampleWithNewData: NewZone = {
  libelleZone: 'afore',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
