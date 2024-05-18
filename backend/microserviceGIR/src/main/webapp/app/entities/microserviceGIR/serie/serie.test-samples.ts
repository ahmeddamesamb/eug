import { ISerie, NewSerie } from './serie.model';

export const sampleWithRequiredData: ISerie = {
  id: 15044,
};

export const sampleWithPartialData: ISerie = {
  id: 31454,
};

export const sampleWithFullData: ISerie = {
  id: 6353,
  codeSerie: 'corkscrew duh lest',
  libelleSerie: 'how around chunder',
  sigleSerie: 'whose',
};

export const sampleWithNewData: NewSerie = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
