import { IOperateur, NewOperateur } from './operateur.model';

export const sampleWithRequiredData: IOperateur = {
  id: 31995,
};

export const sampleWithPartialData: IOperateur = {
  id: 22530,
  userLogin: 'tuber generously',
  codeOperateur: 'opposite slip',
};

export const sampleWithFullData: IOperateur = {
  id: 4824,
  libelle: 'woot shakily',
  userLogin: 'yearly',
  codeOperateur: 'sector useless',
};

export const sampleWithNewData: NewOperateur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
