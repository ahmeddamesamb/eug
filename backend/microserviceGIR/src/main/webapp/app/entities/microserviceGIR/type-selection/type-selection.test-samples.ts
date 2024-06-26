import { ITypeSelection, NewTypeSelection } from './type-selection.model';

export const sampleWithRequiredData: ITypeSelection = {
  id: 3441,
  libelleTypeSelection: 'moyennant autour de',
};

export const sampleWithPartialData: ITypeSelection = {
  id: 32271,
  libelleTypeSelection: 'contourner dès',
};

export const sampleWithFullData: ITypeSelection = {
  id: 3442,
  libelleTypeSelection: 'blême sage au défaut de',
};

export const sampleWithNewData: NewTypeSelection = {
  libelleTypeSelection: 'zzzz',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
