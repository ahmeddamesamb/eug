import dayjs from 'dayjs/esm';

import { IEtudiant, NewEtudiant } from './etudiant.model';

export const sampleWithRequiredData: IEtudiant = {
  id: 32418,
  codeEtu: 'whoever',
  dateNaissEtu: dayjs('2024-05-24'),
  lieuNaissEtu: 'merry boo stealthily',
  sexe: 'fatally assignment but',
  assimileYN: 23714,
  exonereYN: 28659,
};

export const sampleWithPartialData: IEtudiant = {
  id: 6738,
  codeEtu: 'parch grub',
  codeBU: 24261,
  emailUGB: 'linear out greatly',
  dateNaissEtu: dayjs('2024-05-24'),
  lieuNaissEtu: 'the',
  sexe: 'optimisation cylindrical',
  numDocIdentite: 'our affix',
  assimileYN: 16553,
  exonereYN: 23214,
};

export const sampleWithFullData: IEtudiant = {
  id: 20457,
  codeEtu: 'procure',
  ine: 'particularise scary',
  codeBU: 13927,
  emailUGB: 'average',
  dateNaissEtu: dayjs('2024-05-24'),
  lieuNaissEtu: 'consummate like',
  sexe: 'off above impanel',
  numDocIdentite: 'coaxingly furthermore brand',
  assimileYN: 25252,
  exonereYN: 19103,
};

export const sampleWithNewData: NewEtudiant = {
  codeEtu: 'ah crusader fooey',
  dateNaissEtu: dayjs('2024-05-23'),
  lieuNaissEtu: 'mmm wiggle',
  sexe: 'rusty before hm',
  assimileYN: 16016,
  exonereYN: 1450,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
