import { ITypeSelection, NewTypeSelection } from './type-selection.model';

export const sampleWithRequiredData: ITypeSelection = {
  id: 3441,
};

export const sampleWithPartialData: ITypeSelection = {
  id: 29563,
  libelle: 'inasmuch overcharge',
};

export const sampleWithFullData: ITypeSelection = {
  id: 27057,
  libelle: 'during tussle candid',
};

export const sampleWithNewData: NewTypeSelection = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
