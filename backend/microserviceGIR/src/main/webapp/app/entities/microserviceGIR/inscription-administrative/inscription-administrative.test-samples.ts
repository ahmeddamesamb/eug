import { IInscriptionAdministrative, NewInscriptionAdministrative } from './inscription-administrative.model';

export const sampleWithRequiredData: IInscriptionAdministrative = {
  id: 7089,
};

export const sampleWithPartialData: IInscriptionAdministrative = {
  id: 31307,
  repriseYN: 20134,
  autoriseYN: 18503,
  ordreInscription: 26323,
};

export const sampleWithFullData: IInscriptionAdministrative = {
  id: 27626,
  nouveauInscritYN: 262,
  repriseYN: 27752,
  autoriseYN: 28047,
  ordreInscription: 16240,
};

export const sampleWithNewData: NewInscriptionAdministrative = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
