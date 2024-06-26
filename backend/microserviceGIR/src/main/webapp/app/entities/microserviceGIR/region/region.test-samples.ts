import { IRegion, NewRegion } from './region.model';

export const sampleWithRequiredData: IRegion = {
  id: 19453,
  libelleRegion: 'malade préserver dès que',
};

export const sampleWithPartialData: IRegion = {
  id: 8602,
  libelleRegion: 'glouglou égoïste membre titulaire',
};

export const sampleWithFullData: IRegion = {
  id: 27262,
  libelleRegion: 'serviable prestataire de services',
};

export const sampleWithNewData: NewRegion = {
  libelleRegion: 'mentir repasser',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
