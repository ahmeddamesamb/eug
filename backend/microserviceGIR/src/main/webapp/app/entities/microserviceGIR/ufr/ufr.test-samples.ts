import { IUFR, NewUFR } from './ufr.model';

export const sampleWithRequiredData: IUFR = {
  id: 3718,
};

export const sampleWithPartialData: IUFR = {
  id: 4356,
  sigleUFR: 'mid beyond',
};

export const sampleWithFullData: IUFR = {
  id: 9753,
  libeleUFR: 'wrong recent',
  sigleUFR: 'shame',
  systemeLMDYN: 17167,
  ordreStat: 19595,
};

export const sampleWithNewData: NewUFR = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
