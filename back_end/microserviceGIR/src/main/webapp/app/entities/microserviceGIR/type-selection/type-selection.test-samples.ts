import { ITypeSelection, NewTypeSelection } from './type-selection.model';

export const sampleWithRequiredData: ITypeSelection = {
  id: 3441,
  libelleTypeSelection: 'as opposite',
};

export const sampleWithPartialData: ITypeSelection = {
  id: 32271,
  libelleTypeSelection: 'transpire alongside',
};

export const sampleWithFullData: ITypeSelection = {
  id: 3442,
  libelleTypeSelection: 'candid shimmering minus',
};

export const sampleWithNewData: NewTypeSelection = {
  libelleTypeSelection: 'um',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
