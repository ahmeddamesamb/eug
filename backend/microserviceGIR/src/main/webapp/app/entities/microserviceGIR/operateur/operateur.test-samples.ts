import { IOperateur, NewOperateur } from './operateur.model';

export const sampleWithRequiredData: IOperateur = {
  id: 22457,
  libelleOperateur: 'prout',
  userLogin: 'timide souple procéder',
  codeOperateur: 'impromptu gueuler',
};

export const sampleWithPartialData: IOperateur = {
  id: 28171,
  libelleOperateur: 'fusiller',
  userLogin: 'au cas où',
  codeOperateur: 'mince',
};

export const sampleWithFullData: IOperateur = {
  id: 30033,
  libelleOperateur: 'lentement avant que',
  userLogin: 'au-dedans de',
  codeOperateur: 'camarade tic-tac',
  actifYN: false,
};

export const sampleWithNewData: NewOperateur = {
  libelleOperateur: 'ouch ça signaler',
  userLogin: 'lorsque drelin',
  codeOperateur: 'accroître snob',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
