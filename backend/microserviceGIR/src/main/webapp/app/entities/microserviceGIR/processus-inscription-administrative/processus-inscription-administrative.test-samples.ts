import dayjs from 'dayjs/esm';

import { IProcessusInscriptionAdministrative, NewProcessusInscriptionAdministrative } from './processus-inscription-administrative.model';

export const sampleWithRequiredData: IProcessusInscriptionAdministrative = {
  id: 21356,
};

export const sampleWithPartialData: IProcessusInscriptionAdministrative = {
  id: 18254,
  inscriptionDemarreeYN: false,
  dateDemarrageInscription: dayjs('2024-06-25T08:17'),
  inscriptionAnnuleeYN: true,
  dateAnnulationInscription: dayjs('2024-06-25T13:08'),
  apteYN: false,
  dateVisiteMedicale: dayjs('2024-06-25T00:21'),
  beneficiaireCROUSYN: false,
  exporterBUYN: false,
  datePaiementFraisObligatoires: dayjs('2024-06-25T10:37'),
  numeroQuitusCROUS: 3921,
  dateRemiseCarteEtu: dayjs('2024-06-25T11:29'),
  dateRemiseCarteDup: 185,
  dateInscriptionAdministrative: dayjs('2024-06-25T08:14'),
  derniereModification: dayjs('2024-06-25T17:01'),
  inscritOnlineYN: true,
  emailUser: 'précipiter',
};

export const sampleWithFullData: IProcessusInscriptionAdministrative = {
  id: 27233,
  inscriptionDemarreeYN: false,
  dateDemarrageInscription: dayjs('2024-06-25T19:14'),
  inscriptionAnnuleeYN: true,
  dateAnnulationInscription: dayjs('2024-06-25T06:25'),
  apteYN: false,
  dateVisiteMedicale: dayjs('2024-06-24T23:27'),
  beneficiaireCROUSYN: false,
  dateRemiseQuitusCROUS: dayjs('2024-06-25T01:31'),
  nouveauCodeBUYN: false,
  quitusBUYN: false,
  dateRemiseQuitusBU: dayjs('2024-06-25T00:45'),
  exporterBUYN: true,
  fraisObligatoiresPayesYN: true,
  datePaiementFraisObligatoires: dayjs('2024-06-25T08:20'),
  numeroQuitusCROUS: 21631,
  carteEturemiseYN: true,
  carteDupremiseYN: true,
  dateRemiseCarteEtu: dayjs('2024-06-24T23:58'),
  dateRemiseCarteDup: 12605,
  inscritAdministrativementYN: true,
  dateInscriptionAdministrative: dayjs('2024-06-25T20:30'),
  derniereModification: dayjs('2024-06-25T02:05'),
  inscritOnlineYN: false,
  emailUser: 'bien que propre',
};

export const sampleWithNewData: NewProcessusInscriptionAdministrative = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
