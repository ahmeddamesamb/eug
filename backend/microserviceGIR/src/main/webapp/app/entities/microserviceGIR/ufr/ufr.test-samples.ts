import { IUfr, NewUfr } from './ufr.model';

export const sampleWithRequiredData: IUfr = {
  id: 22926,
  libelleUfr: 'ha ha gratis',
  sigleUfr: 'cracher',
};

export const sampleWithPartialData: IUfr = {
  id: 32551,
  libelleUfr: 'fade',
  sigleUfr: 'au cas où',
  actifYN: false,
};

export const sampleWithFullData: IUfr = {
  id: 18988,
  libelleUfr: 'à condition que pas mal parmi',
  sigleUfr: 'attirer infime',
  prefixe: 'porte-parole à force de coac coac',
  actifYN: false,
};

export const sampleWithNewData: NewUfr = {
  libelleUfr: "à l'exception de bien que",
  sigleUfr: 'collègue',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
