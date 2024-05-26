import { INiveau, NewNiveau } from './niveau.model';

export const sampleWithRequiredData: INiveau = {
  id: 23317,
  libelleNiveau: 'organic oxygen burly',
  cycleNiveau: 'MASTER',
  codeNiveau: 'toll of bah',
};

export const sampleWithPartialData: INiveau = {
  id: 9495,
  libelleNiveau: 'round repeatedly tempting',
  cycleNiveau: 'MASTER',
  codeNiveau: 'towards',
  anneeEtude: 'until',
};

export const sampleWithFullData: INiveau = {
  id: 1398,
  libelleNiveau: 'abrogation',
  cycleNiveau: 'DOCTOTAT',
  codeNiveau: 'hence',
  anneeEtude: 'stag',
};

export const sampleWithNewData: NewNiveau = {
  libelleNiveau: 'phew drat yowza',
  cycleNiveau: 'MASTER',
  codeNiveau: 'rudely compel',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
