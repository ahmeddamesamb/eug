import { IRegion, NewRegion } from './region.model';

export const sampleWithRequiredData: IRegion = {
  id: 19453,
  libelleRegion: 'musty lug whether',
};

export const sampleWithPartialData: IRegion = {
  id: 8602,
  libelleRegion: 'woot every infix',
};

export const sampleWithFullData: IRegion = {
  id: 27262,
  libelleRegion: 'sour guilt',
};

export const sampleWithNewData: NewRegion = {
  libelleRegion: 'interlink radicalise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
