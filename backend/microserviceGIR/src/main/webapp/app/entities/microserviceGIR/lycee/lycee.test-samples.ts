import { ILycee, NewLycee } from './lycee.model';

export const sampleWithRequiredData: ILycee = {
  id: 24129,
  nomLycee: 'clientèle police ouin',
};

export const sampleWithPartialData: ILycee = {
  id: 13827,
  nomLycee: 'conseil d’administration',
  villeLycee: 'vlan après-demain',
  centreExamen: 'par',
};

export const sampleWithFullData: ILycee = {
  id: 30785,
  nomLycee: 'loin direction',
  codeLycee: 'vu que dès que',
  villeLycee: 'à partir de moyennant',
  academieLycee: 27098,
  centreExamen: 'loin de bzzz',
};

export const sampleWithNewData: NewLycee = {
  nomLycee: 'quelque moyennant dehors',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
