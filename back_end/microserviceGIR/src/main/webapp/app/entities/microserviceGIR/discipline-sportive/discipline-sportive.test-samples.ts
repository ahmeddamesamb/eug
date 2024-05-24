import { IDisciplineSportive, NewDisciplineSportive } from './discipline-sportive.model';

export const sampleWithRequiredData: IDisciplineSportive = {
  id: 23614,
  libelleDisciplineSportive: 'down jeer around',
};

export const sampleWithPartialData: IDisciplineSportive = {
  id: 13219,
  libelleDisciplineSportive: 'bench aside amid',
};

export const sampleWithFullData: IDisciplineSportive = {
  id: 14288,
  libelleDisciplineSportive: 'yahoo freeload opossum',
};

export const sampleWithNewData: NewDisciplineSportive = {
  libelleDisciplineSportive: 'naming infer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
