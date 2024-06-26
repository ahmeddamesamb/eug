import { IInformationImage, NewInformationImage } from './information-image.model';

export const sampleWithRequiredData: IInformationImage = {
  id: 2016,
  cheminPath: 'toc-toc',
  cheminFile: 'sauf à partenaire',
};

export const sampleWithPartialData: IInformationImage = {
  id: 26580,
  cheminPath: 'sous couleur de',
  cheminFile: 'rose à la faveur de comme',
  enCoursYN: 'carrément',
};

export const sampleWithFullData: IInformationImage = {
  id: 14615,
  description: 'vers admirablement en',
  cheminPath: 'exiger diplomate prestataire de services',
  cheminFile: 'embarquer au cas où',
  enCoursYN: 'diététiste coac coac beaucoup',
};

export const sampleWithNewData: NewInformationImage = {
  cheminPath: 'zzzz',
  cheminFile: 'vers que',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
