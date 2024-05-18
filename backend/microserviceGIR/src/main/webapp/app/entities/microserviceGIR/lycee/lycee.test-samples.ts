import { ILycee, NewLycee } from './lycee.model';

export const sampleWithRequiredData: ILycee = {
  id: 24129,
};

export const sampleWithPartialData: ILycee = {
  id: 26086,
  academieLycee: 2263,
  centreExamen: 'stepmother gadzooks',
};

export const sampleWithFullData: ILycee = {
  id: 24459,
  nomLycee: 'kit oof',
  codeLycee: 'squiggly ack',
  villeLycee: 'um blacken',
  academieLycee: 13288,
  centreExamen: 'astride',
};

export const sampleWithNewData: NewLycee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
