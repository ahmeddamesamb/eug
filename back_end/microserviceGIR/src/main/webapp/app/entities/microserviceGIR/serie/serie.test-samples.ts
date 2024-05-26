import { ISerie, NewSerie } from './serie.model';

export const sampleWithRequiredData: ISerie = {
  id: 15044,
  libelleSerie: 'aha busily fast',
  sigleSerie: 'bah now knavishly',
};

export const sampleWithPartialData: ISerie = {
  id: 31114,
  codeSerie: 'for',
  libelleSerie: 'union condescend',
  sigleSerie: 'after against meanwhile',
};

export const sampleWithFullData: ISerie = {
  id: 31744,
  codeSerie: 'shakily',
  libelleSerie: 'orient winged',
  sigleSerie: 'throughout',
};

export const sampleWithNewData: NewSerie = {
  libelleSerie: 'certainly',
  sigleSerie: 'than utilized',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
