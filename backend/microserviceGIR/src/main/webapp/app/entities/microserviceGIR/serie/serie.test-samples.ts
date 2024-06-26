import { ISerie, NewSerie } from './serie.model';

export const sampleWithRequiredData: ISerie = {
  id: 15044,
  libelleSerie: 'euh incognito diablement',
  sigleSerie: 'ouille à condition que totalement',
};

export const sampleWithPartialData: ISerie = {
  id: 31114,
  codeSerie: 'puisque',
  libelleSerie: 'rédaction souffrir',
  sigleSerie: 'de de si',
};

export const sampleWithFullData: ISerie = {
  id: 31744,
  codeSerie: 'alentour',
  libelleSerie: 'subsister vide',
  sigleSerie: 'en dépit de',
};

export const sampleWithNewData: NewSerie = {
  libelleSerie: 'mal',
  sigleSerie: 'tant timide',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
