import { INiveau, NewNiveau } from './niveau.model';

export const sampleWithRequiredData: INiveau = {
  id: 23317,
  libelleNiveau: 'minuscule clientèle blême',
  codeNiveau: 'sécher jeune',
};

export const sampleWithPartialData: INiveau = {
  id: 27344,
  libelleNiveau: 'mairie',
  codeNiveau: 'tendre tandis que',
  actifYN: false,
};

export const sampleWithFullData: INiveau = {
  id: 19461,
  libelleNiveau: "meuh à l'entour de areu areu",
  codeNiveau: 'cadre',
  anneeEtude: 'assigner même si oups',
  actifYN: false,
};

export const sampleWithNewData: NewNiveau = {
  libelleNiveau: 'avant que',
  codeNiveau: 'bof collègue',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
