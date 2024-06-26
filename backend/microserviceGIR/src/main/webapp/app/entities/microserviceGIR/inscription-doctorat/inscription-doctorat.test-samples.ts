import { IInscriptionDoctorat, NewInscriptionDoctorat } from './inscription-doctorat.model';

export const sampleWithRequiredData: IInscriptionDoctorat = {
  id: 28652,
};

export const sampleWithPartialData: IInscriptionDoctorat = {
  id: 15437,
  sourceFinancement: 'presque',
  coEncadreurId: 'minuscule',
  nombreInscription: 9157,
};

export const sampleWithFullData: IInscriptionDoctorat = {
  id: 27053,
  sourceFinancement: 'aider',
  coEncadreurId: 'compliquer insipide',
  nombreInscription: 9241,
};

export const sampleWithNewData: NewInscriptionDoctorat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
