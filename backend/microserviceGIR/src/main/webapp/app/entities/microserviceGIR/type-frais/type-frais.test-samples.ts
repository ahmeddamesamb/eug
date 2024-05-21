import { ITypeFrais, NewTypeFrais } from './type-frais.model';

export const sampleWithRequiredData: ITypeFrais = {
  id: 12844,
};

export const sampleWithPartialData: ITypeFrais = {
  id: 23968,
};

export const sampleWithFullData: ITypeFrais = {
  id: 24852,
  libelle: 'at after',
};

export const sampleWithNewData: NewTypeFrais = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
