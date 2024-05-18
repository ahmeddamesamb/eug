import { IDisciplineSportive, NewDisciplineSportive } from './discipline-sportive.model';

export const sampleWithRequiredData: IDisciplineSportive = {
  id: 23614,
};

export const sampleWithPartialData: IDisciplineSportive = {
  id: 10184,
};

export const sampleWithFullData: IDisciplineSportive = {
  id: 4651,
  libelleDisciplineSportive: 'before diplomacy',
};

export const sampleWithNewData: NewDisciplineSportive = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
