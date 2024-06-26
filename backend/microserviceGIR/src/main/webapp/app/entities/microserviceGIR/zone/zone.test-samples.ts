import { IZone, NewZone } from './zone.model';

export const sampleWithRequiredData: IZone = {
  id: 27060,
  libelleZone: 'bzzz assez',
};

export const sampleWithPartialData: IZone = {
  id: 391,
  libelleZone: 'fourbe',
};

export const sampleWithFullData: IZone = {
  id: 23836,
  libelleZone: 'battre contre pendant que',
};

export const sampleWithNewData: NewZone = {
  libelleZone: 'contre',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
