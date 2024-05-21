import { INiveau, NewNiveau } from './niveau.model';

export const sampleWithRequiredData: INiveau = {
  id: 23317,
};

export const sampleWithPartialData: INiveau = {
  id: 18081,
  cycleNiveau: 'drain replacement across',
  codeNiveau: 'impressive majestically though',
};

export const sampleWithFullData: INiveau = {
  id: 12214,
  libelleNiveau: 'plump nickel whose',
  cycleNiveau: 'for oh',
  codeNiveau: 'abrogation',
  anneeEtude: 'tolerate instead gadzooks',
};

export const sampleWithNewData: NewNiveau = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
