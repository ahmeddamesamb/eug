import { IOperateur, NewOperateur } from './operateur.model';

export const sampleWithRequiredData: IOperateur = {
  id: 31995,
  libelleOperateur: 'and well-documented grounded',
  userLogin: 'lend after bit',
  codeOperateur: 'sometimes or',
};

export const sampleWithPartialData: IOperateur = {
  id: 32562,
  libelleOperateur: 'hmph',
  userLogin: 'blissful nearly',
  codeOperateur: 'even',
};

export const sampleWithFullData: IOperateur = {
  id: 30119,
  libelleOperateur: 'doting thread',
  userLogin: 'thin',
  codeOperateur: 'smoothly',
};

export const sampleWithNewData: NewOperateur = {
  libelleOperateur: 'shield',
  userLogin: 'um',
  codeOperateur: 'pace vogue',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
