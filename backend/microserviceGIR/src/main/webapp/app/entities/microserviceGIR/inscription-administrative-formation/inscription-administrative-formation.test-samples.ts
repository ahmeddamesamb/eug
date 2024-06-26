import dayjs from 'dayjs/esm';

import { IInscriptionAdministrativeFormation, NewInscriptionAdministrativeFormation } from './inscription-administrative-formation.model';

export const sampleWithRequiredData: IInscriptionAdministrativeFormation = {
  id: 17105,
  exonereYN: true,
};

export const sampleWithPartialData: IInscriptionAdministrativeFormation = {
  id: 23583,
  inscriptionPrincipaleYN: true,
  exonereYN: false,
  paiementFraisOblYN: true,
};

export const sampleWithFullData: IInscriptionAdministrativeFormation = {
  id: 4187,
  inscriptionPrincipaleYN: true,
  inscriptionAnnuleeYN: true,
  exonereYN: true,
  paiementFraisOblYN: false,
  paiementFraisIntegergYN: false,
  certificatDelivreYN: true,
  dateChoixFormation: dayjs('2024-06-25T13:28'),
  dateValidationInscription: dayjs('2024-06-24T21:16'),
};

export const sampleWithNewData: NewInscriptionAdministrativeFormation = {
  exonereYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
