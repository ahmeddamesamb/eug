import { IInscriptionAdministrative, NewInscriptionAdministrative } from './inscription-administrative.model';

export const sampleWithRequiredData: IInscriptionAdministrative = {
  id: 7089,
};

export const sampleWithPartialData: IInscriptionAdministrative = {
  id: 31307,
  repriseYN: false,
  autoriseYN: false,
  ordreInscription: 26323,
};

export const sampleWithFullData: IInscriptionAdministrative = {
  id: 27626,
  nouveauInscritYN: true,
  repriseYN: false,
  autoriseYN: false,
  ordreInscription: 16240,
};

export const sampleWithNewData: NewInscriptionAdministrative = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
