import dayjs from 'dayjs/esm';

import {
  IProcessusDinscriptionAdministrative,
  NewProcessusDinscriptionAdministrative,
} from './processus-dinscription-administrative.model';

export const sampleWithRequiredData: IProcessusDinscriptionAdministrative = {
  id: 6775,
};

export const sampleWithPartialData: IProcessusDinscriptionAdministrative = {
  id: 24462,
  inscriptionDemarreeYN: 8187,
  dateRemiseQuitusCROUS: dayjs('2024-05-24T17:33'),
  nouveauCodeBUYN: 30418,
  quitusBUYN: 23687,
  numeroQuitusCROUS: 26851,
  carteEturemiseYN: 13742,
  dateRemiseCarteEtu: dayjs('2024-05-23T21:43'),
  dateRemiseCarteDup: 8313,
  emailUser: 'circumvent',
};

export const sampleWithFullData: IProcessusDinscriptionAdministrative = {
  id: 8622,
  inscriptionDemarreeYN: 21229,
  dateDemarrageInscription: dayjs('2024-05-24T09:38'),
  inscriptionAnnuleeYN: 5752,
  dateAnnulationInscription: dayjs('2024-05-24T03:51'),
  apteYN: 29486,
  dateVisiteMedicale: dayjs('2024-05-24T10:18'),
  beneficiaireCROUSYN: 3619,
  dateRemiseQuitusCROUS: dayjs('2024-05-24T11:17'),
  nouveauCodeBUYN: 27973,
  quitusBUYN: 18877,
  dateRemiseQuitusBU: dayjs('2024-05-24T12:20'),
  exporterBUYN: 5786,
  fraisObligatoiresPayesYN: 5887,
  datePaiementFraisObligatoires: dayjs('2024-05-23T22:26'),
  numeroQuitusCROUS: 10622,
  carteEturemiseYN: 17197,
  carteDupremiseYN: 10585,
  dateRemiseCarteEtu: dayjs('2024-05-24T06:31'),
  dateRemiseCarteDup: 12978,
  inscritAdministrativementYN: 1562,
  dateInscriptionAdministrative: dayjs('2024-05-24T05:43'),
  derniereModification: dayjs('2024-05-24T13:57'),
  inscritOnlineYN: 4111,
  emailUser: 'rapidly tremendously over',
};

export const sampleWithNewData: NewProcessusDinscriptionAdministrative = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
