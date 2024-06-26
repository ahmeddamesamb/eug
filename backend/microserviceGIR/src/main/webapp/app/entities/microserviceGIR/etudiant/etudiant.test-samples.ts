import dayjs from 'dayjs/esm';

import { IEtudiant, NewEtudiant } from './etudiant.model';

export const sampleWithRequiredData: IEtudiant = {
  id: 32418,
  codeEtu: 'tant que',
  dateNaissEtu: dayjs('2024-06-25'),
  lieuNaissEtu: 'magenta bang ça',
  sexe: 'divinement commis de cuisine de manière à ce que',
  assimileYN: false,
};

export const sampleWithPartialData: IEtudiant = {
  id: 6738,
  codeEtu: 'contribuer marier',
  emailUGB: 'vide après que assez',
  dateNaissEtu: dayjs('2024-06-25'),
  lieuNaissEtu: 'rédaction gens propre',
  sexe: 'novice soupçonner plouf',
  numDocIdentite: 'trop touchant',
  assimileYN: true,
  actifYN: false,
};

export const sampleWithFullData: IEtudiant = {
  id: 29947,
  codeEtu: 'taire autour de',
  ine: 'trop',
  codeBU: 'camarade minuscule',
  emailUGB: 'à demi',
  dateNaissEtu: dayjs('2024-06-25'),
  lieuNaissEtu: 'tâter',
  sexe: 'tant',
  numDocIdentite: 'de fort',
  assimileYN: false,
  actifYN: true,
};

export const sampleWithNewData: NewEtudiant = {
  codeEtu: 'pschitt hé partenaire',
  dateNaissEtu: dayjs('2024-06-25'),
  lieuNaissEtu: 'partir prestataire de services',
  sexe: 'badaboum pin-pon vraisemblablement',
  assimileYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
