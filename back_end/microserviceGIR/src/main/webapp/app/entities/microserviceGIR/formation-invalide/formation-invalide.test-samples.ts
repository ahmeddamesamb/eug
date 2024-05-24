import { IFormationInvalide, NewFormationInvalide } from './formation-invalide.model';

export const sampleWithRequiredData: IFormationInvalide = {
  id: 2530,
};

export const sampleWithPartialData: IFormationInvalide = {
  id: 30844,
  actifYN: 6341,
};

export const sampleWithFullData: IFormationInvalide = {
  id: 3774,
  actifYN: 3522,
};

export const sampleWithNewData: NewFormationInvalide = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
