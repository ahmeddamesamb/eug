import dayjs from 'dayjs/esm';

import { IEtudiant, NewEtudiant } from './etudiant.model';

export const sampleWithRequiredData: IEtudiant = {
  id: 32418,
  codeEtu: 'whoever',
  ine: 'majestic sew',
  codeBU: 2909,
  lieuNaissEtu: 'relish fatally assignment',
  sexe: 'shakily jovially parch',
  assimileYN: 10242,
  exonereYN: 28059,
};

export const sampleWithPartialData: IEtudiant = {
  id: 15439,
  codeEtu: 'necessary gah',
  ine: 'doubtfully as flat',
  codeBU: 22522,
  lieuNaissEtu: 'remote opposite refill',
  sexe: 'ouch once',
  assimileYN: 22377,
  exonereYN: 17477,
};

export const sampleWithFullData: IEtudiant = {
  id: 17778,
  codeEtu: 'who with preserve',
  ine: 'rapidly sudden',
  codeBU: 16380,
  emailUGB: 'ornate',
  dateNaissEtu: dayjs('2024-05-18'),
  lieuNaissEtu: 'boo immunise',
  sexe: 'dilate ha apparatus',
  numDocIdentite: 'murmuring',
  assimileYN: 21936,
  exonereYN: 4393,
};

export const sampleWithNewData: NewEtudiant = {
  codeEtu: 'when mine',
  ine: 'cherry',
  codeBU: 938,
  lieuNaissEtu: 'half-brother',
  sexe: 'blah ha yieldingly',
  assimileYN: 25284,
  exonereYN: 21442,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
