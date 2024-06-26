import { IDisciplineSportive, NewDisciplineSportive } from './discipline-sportive.model';

export const sampleWithRequiredData: IDisciplineSportive = {
  id: 23614,
  libelleDisciplineSportive: 'à défaut de  recourir malgré',
};

export const sampleWithPartialData: IDisciplineSportive = {
  id: 13219,
  libelleDisciplineSportive: 'diplomate nonobstant dès',
};

export const sampleWithFullData: IDisciplineSportive = {
  id: 14288,
  libelleDisciplineSportive: 'euh communiquer équipe de recherche',
};

export const sampleWithNewData: NewDisciplineSportive = {
  libelleDisciplineSportive: 'administration rassurer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
