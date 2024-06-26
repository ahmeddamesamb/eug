import { IFormationInvalide, NewFormationInvalide } from './formation-invalide.model';

export const sampleWithRequiredData: IFormationInvalide = {
  id: 2530,
};

export const sampleWithPartialData: IFormationInvalide = {
  id: 30844,
  actifYN: true,
};

export const sampleWithFullData: IFormationInvalide = {
  id: 3774,
  actifYN: true,
};

export const sampleWithNewData: NewFormationInvalide = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
