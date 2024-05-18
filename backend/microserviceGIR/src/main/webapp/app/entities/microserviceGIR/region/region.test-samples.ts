import { IRegion, NewRegion } from './region.model';

export const sampleWithRequiredData: IRegion = {
  id: 19453,
};

export const sampleWithPartialData: IRegion = {
  id: 24382,
};

export const sampleWithFullData: IRegion = {
  id: 5719,
  libelleRegion: 'unto positive',
};

export const sampleWithNewData: NewRegion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
