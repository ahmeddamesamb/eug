import dayjs from 'dayjs/esm';

import { IInformationPersonnelle, NewInformationPersonnelle } from './information-personnelle.model';

export const sampleWithRequiredData: IInformationPersonnelle = {
  id: 13605,
  nomEtu: 'exciting',
  prenomEtu: 'smoothly ashamed dental',
  statutMarital: 'psst or',
  adresseEtu: 'knowledgeably who emit',
};

export const sampleWithPartialData: IInformationPersonnelle = {
  id: 23050,
  nomEtu: 'scary on',
  prenomEtu: 'unless',
  statutMarital: 'knickers',
  regime: 4494,
  profession: 'quixotic where before',
  adresseEtu: 'zowie',
  emailEtu: 'psst athwart upliftingly',
  telParent: 'officially espy eek',
  nomParent: 'failing low even',
  prenomParent: 'overeat ink ack',
  handicapYN: 4807,
  derniereModification: dayjs('2024-05-18'),
  emailUser: 'per',
};

export const sampleWithFullData: IInformationPersonnelle = {
  id: 2580,
  nomEtu: 'boo combine pun',
  nomJeuneFilleEtu: 'supposing welcome',
  prenomEtu: 'eek aboard',
  statutMarital: 'vigilant',
  regime: 23257,
  profession: 'harmonious',
  adresseEtu: 'positively while wrongly',
  telEtu: 'wonderfully',
  emailEtu: 'stare mellow brr',
  adresseParent: 'ideate archer',
  telParent: 'after',
  emailParent: 'abaft',
  nomParent: 'glorious yuck reciprocity',
  prenomParent: 'separately qua who',
  handicapYN: 11057,
  ordiPersoYN: 10192,
  derniereModification: dayjs('2024-05-18'),
  emailUser: 'aw boo',
};

export const sampleWithNewData: NewInformationPersonnelle = {
  nomEtu: 'woefully',
  prenomEtu: 'yahoo officially questioningly',
  statutMarital: 'voluminous gracious',
  adresseEtu: 'off lobby past',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
