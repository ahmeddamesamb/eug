import { ITypeFormation, NewTypeFormation } from './type-formation.model';

export const sampleWithRequiredData: ITypeFormation = {
  id: 22015,
  libelleTypeFormation: 'pendant que auprès de',
};

export const sampleWithPartialData: ITypeFormation = {
  id: 19703,
  libelleTypeFormation: 'commis de cuisine rejeter ennuyer',
};

export const sampleWithFullData: ITypeFormation = {
  id: 30527,
  libelleTypeFormation: 'autrement sombrer',
};

export const sampleWithNewData: NewTypeFormation = {
  libelleTypeFormation: 'plier toc jeter',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
