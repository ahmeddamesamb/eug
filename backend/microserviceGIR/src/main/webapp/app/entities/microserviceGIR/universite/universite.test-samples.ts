import { IUniversite, NewUniversite } from './universite.model';

export const sampleWithRequiredData: IUniversite = {
  id: 9926,
};

export const sampleWithPartialData: IUniversite = {
  id: 25221,
  nomUniversite: 'among scarcely unknown',
  sigleUniversite: 'afterwards',
};

export const sampleWithFullData: IUniversite = {
  id: 3794,
  nomUniversite: 'warp',
  sigleUniversite: 'quietly peruse on',
};

export const sampleWithNewData: NewUniversite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
