import { IUfr, NewUfr } from './ufr.model';

export const sampleWithRequiredData: IUfr = {
  id: 22926,
  libeleUfr: 'meh briefly',
  sigleUfr: 'prowl',
  systemeLMDYN: 28531,
};

export const sampleWithPartialData: IUfr = {
  id: 32551,
  libeleUfr: 'frank',
  sigleUfr: 'or',
  systemeLMDYN: 19595,
  ordreStat: 18988,
};

export const sampleWithFullData: IUfr = {
  id: 26339,
  libeleUfr: 'past',
  sigleUfr: 'for lest',
  systemeLMDYN: 25795,
  ordreStat: 9025,
};

export const sampleWithNewData: NewUfr = {
  libeleUfr: 'with enthral tenderly',
  sigleUfr: 'militate oh everlasting',
  systemeLMDYN: 27742,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
