import { ITypeFrais, NewTypeFrais } from './type-frais.model';

export const sampleWithRequiredData: ITypeFrais = {
  id: 12844,
  libelleTypeFrais: 'mortally converse when',
};

export const sampleWithPartialData: ITypeFrais = {
  id: 29275,
  libelleTypeFrais: 'inside',
};

export const sampleWithFullData: ITypeFrais = {
  id: 21692,
  libelleTypeFrais: 'upward silver',
};

export const sampleWithNewData: NewTypeFrais = {
  libelleTypeFrais: 'tight equally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
