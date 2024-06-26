import { ITypeRapport, NewTypeRapport } from './type-rapport.model';

export const sampleWithRequiredData: ITypeRapport = {
  id: 28939,
};

export const sampleWithPartialData: ITypeRapport = {
  id: 20373,
  libelleTypeRapport: 'quasiment',
};

export const sampleWithFullData: ITypeRapport = {
  id: 1247,
  libelleTypeRapport: 'si bien que rectorat',
};

export const sampleWithNewData: NewTypeRapport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
