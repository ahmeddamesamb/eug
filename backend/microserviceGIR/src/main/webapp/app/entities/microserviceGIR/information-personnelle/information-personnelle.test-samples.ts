import dayjs from 'dayjs/esm';

import { IInformationPersonnelle, NewInformationPersonnelle } from './information-personnelle.model';

export const sampleWithRequiredData: IInformationPersonnelle = {
  id: 2544,
  nomEtu: 'tant que au-dessus aimable',
  prenomEtu: 'déjà hi',
  statutMarital: 'sous couleur de tout',
  adresseEtu: 'parce que y assez',
};

export const sampleWithPartialData: IInformationPersonnelle = {
  id: 22030,
  nomEtu: 'pourvu que pour que',
  nomJeuneFilleEtu: 'biathlète',
  prenomEtu: 'placide aussitôt que de manière à ce que',
  statutMarital: 'toc',
  regime: 32753,
  profession: 'hôte',
  adresseEtu: 'soit dring',
  emailEtu: 'bien que chef de cuisine splendide',
  adresseParent: 'si bien que',
  emailParent: 'encore vraiment spécialiste',
  handicapYN: false,
  ordiPersoYN: false,
};

export const sampleWithFullData: IInformationPersonnelle = {
  id: 3105,
  nomEtu: 'à force de',
  nomJeuneFilleEtu: 'vis-à-vie de dessus responsable',
  prenomEtu: 'pis',
  statutMarital: 'membre de l’équipe pendre turquoise',
  regime: 15602,
  profession: 'groin groin euh',
  adresseEtu: 'quelquefois timide',
  telEtu: 'réjouir hirsute',
  emailEtu: 'aller plic mince',
  adresseParent: 'obtenir ouch',
  telParent: 'contenter',
  emailParent: 'cadre cocorico',
  nomParent: 'dévoiler défaire personnel professionnel',
  prenomParent: 'au défaut de',
  handicapYN: false,
  photo: 'dense vroum de manière à ce que',
  ordiPersoYN: false,
  derniereModification: dayjs('2024-06-25'),
  emailUser: 'foule puis secours',
};

export const sampleWithNewData: NewInformationPersonnelle = {
  nomEtu: 'membre de l’équipe commis répandre',
  prenomEtu: 'vide',
  statutMarital: 'habile administration',
  adresseEtu: 'à partir de',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
