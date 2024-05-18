import dayjs from 'dayjs/esm';

import { IInscriptionAdministrativeFormation, NewInscriptionAdministrativeFormation } from './inscription-administrative-formation.model';

export const sampleWithRequiredData: IInscriptionAdministrativeFormation = {
  id: 13339,
};

export const sampleWithPartialData: IInscriptionAdministrativeFormation = {
  id: 23461,
  inscriptionAnnuleeYN: 29571,
  paiementFraisOblYN: 23583,
  certificatDelivreYN: 10009,
};

export const sampleWithFullData: IInscriptionAdministrativeFormation = {
  id: 16994,
  inscriptionPrincipaleYN: 1659,
  inscriptionAnnuleeYN: 4187,
  paiementFraisOblYN: 7790,
  paiementFraisIntegergYN: 9365,
  certificatDelivreYN: 774,
  dateChoixFormation: dayjs('2024-05-18T02:21'),
  dateValidationInscription: dayjs('2024-05-17T17:13'),
};

export const sampleWithNewData: NewInscriptionAdministrativeFormation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
