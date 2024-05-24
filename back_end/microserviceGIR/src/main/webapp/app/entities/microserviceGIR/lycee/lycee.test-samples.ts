import { ILycee, NewLycee } from './lycee.model';

export const sampleWithRequiredData: ILycee = {
  id: 24129,
  nomLycee: 'panther stepmother gadzooks',
};

export const sampleWithPartialData: ILycee = {
  id: 13827,
  nomLycee: 'oasis',
  villeLycee: 'ick mechanically',
  centreExamen: 'athwart',
};

export const sampleWithFullData: ILycee = {
  id: 30785,
  nomLycee: 'unexpectedly protein',
  codeLycee: 'how whether',
  villeLycee: 'into as',
  academieLycee: 27098,
  centreExamen: 'until boohoo',
};

export const sampleWithNewData: NewLycee = {
  nomLycee: 'jealously as terribly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
