import { ITypeFrais, NewTypeFrais } from './type-frais.model';

export const sampleWithRequiredData: ITypeFrais = {
  id: 12844,
  libelleTypeFrais: 'autrefois traiter lorsque',
};

export const sampleWithPartialData: ITypeFrais = {
  id: 29275,
  libelleTypeFrais: 'à moins de',
};

export const sampleWithFullData: ITypeFrais = {
  id: 21692,
  libelleTypeFrais: 'près sauvage',
};

export const sampleWithNewData: NewTypeFrais = {
  libelleTypeFrais: 'splendide beaucoup',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
