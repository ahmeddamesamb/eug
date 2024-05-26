import dayjs from 'dayjs/esm';

import { IInformationPersonnelle, NewInformationPersonnelle } from './information-personnelle.model';

export const sampleWithRequiredData: IInformationPersonnelle = {
  id: 2544,
  nomEtu: 'who smoothly ashamed',
  prenomEtu: 'oddly psst',
  statutMarital: 'without knowledgeably',
  adresseEtu: 'furthermore usually till',
};

export const sampleWithPartialData: IInformationPersonnelle = {
  id: 22030,
  nomEtu: 'or and',
  nomJeuneFilleEtu: 'catch',
  prenomEtu: 'quixotic where before',
  statutMarital: 'zowie',
  regime: 32753,
  profession: 'hoof',
  adresseEtu: 'worriedly yahoo',
  emailEtu: 'likewise collectivisation tight',
  adresseParent: 'since',
  emailParent: 'fondly yesterday geometry',
  handicapYN: 21183,
  ordiPersoYN: 30164,
};

export const sampleWithFullData: IInformationPersonnelle = {
  id: 3105,
  nomEtu: 'except',
  nomJeuneFilleEtu: 'worth too cinder',
  prenomEtu: 'closely',
  statutMarital: 'frequency reassert wealthy',
  regime: 15602,
  profession: 'drat aha',
  adresseEtu: 'repeatedly useless',
  telEtu: 'spear homely',
  emailEtu: 'accuse mmm old-fashioned',
  adresseParent: 'average eek',
  telParent: 'grab',
  emailParent: 'afoul pfft',
  nomParent: 'withhold pave talking',
  prenomParent: 'minus',
  handicapYN: 32239,
  photo: 'distorted oof but',
  ordiPersoYN: 19712,
  derniereModification: dayjs('2024-05-24'),
  emailUser: 'washbasin readily veterinarian',
};

export const sampleWithNewData: NewInformationPersonnelle = {
  nomEtu: 'formamide judgment overshoot',
  prenomEtu: 'wonderful',
  statutMarital: 'gummy museum',
  adresseEtu: 'inside',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
