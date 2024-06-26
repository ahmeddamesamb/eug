import { IDisciplineSportiveEtudiant, NewDisciplineSportiveEtudiant } from './discipline-sportive-etudiant.model';

export const sampleWithRequiredData: IDisciplineSportiveEtudiant = {
  id: 1815,
};

export const sampleWithPartialData: IDisciplineSportiveEtudiant = {
  id: 30349,
};

export const sampleWithFullData: IDisciplineSportiveEtudiant = {
  id: 9102,
  licenceSportiveYN: true,
  competitionUGBYN: true,
};

export const sampleWithNewData: NewDisciplineSportiveEtudiant = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
