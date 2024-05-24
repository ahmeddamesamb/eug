import { IUniversite, NewUniversite } from './universite.model';

export const sampleWithRequiredData: IUniversite = {
  id: 9926,
  nomUniversite: 'um anenst',
  sigleUniversite: 'poised weekly amongst',
};

export const sampleWithPartialData: IUniversite = {
  id: 10652,
  nomUniversite: 'affectionate',
  sigleUniversite: 'deal known progress',
};

export const sampleWithFullData: IUniversite = {
  id: 7139,
  nomUniversite: 'broadly',
  sigleUniversite: 'alongside yahoo',
};

export const sampleWithNewData: NewUniversite = {
  nomUniversite: 'ick extemporize',
  sigleUniversite: 'absent',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
