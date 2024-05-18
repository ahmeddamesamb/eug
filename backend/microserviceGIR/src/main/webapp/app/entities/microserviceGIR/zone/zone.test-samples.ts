import { IZone, NewZone } from './zone.model';

export const sampleWithRequiredData: IZone = {
  id: 27060,
};

export const sampleWithPartialData: IZone = {
  id: 8448,
};

export const sampleWithFullData: IZone = {
  id: 4133,
  libelleZone: 'processor properly what',
};

export const sampleWithNewData: NewZone = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
