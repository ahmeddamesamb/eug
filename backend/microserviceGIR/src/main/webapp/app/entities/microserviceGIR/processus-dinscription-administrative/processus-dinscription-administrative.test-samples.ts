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
  dateRemiseQuitusCROUS: dayjs('2024-05-18T15:17'),
  nouveauCodeBUYN: 30418,
  quitusBUYN: 23687,
  numeroQuitusCROUS: 26851,
  carteEturemiseYN: 13742,
  dateRemiseCarteEtu: dayjs('2024-05-17T19:28'),
  dateRemiseCarteDup: 8313,
  emailUser: 'circumvent',
};

export const sampleWithFullData: IProcessusDinscriptionAdministrative = {
  id: 8622,
  inscriptionDemarreeYN: 21229,
  dateDemarrageInscription: dayjs('2024-05-18T07:22'),
  inscriptionAnnuleeYN: 5752,
  dateAnnulationInscription: dayjs('2024-05-18T01:35'),
  apteYN: 29486,
  dateVisiteMedicale: dayjs('2024-05-18T08:02'),
  beneficiaireCROUSYN: 3619,
  dateRemiseQuitusCROUS: dayjs('2024-05-18T09:01'),
  nouveauCodeBUYN: 27973,
  quitusBUYN: 18877,
  dateRemiseQuitusBU: dayjs('2024-05-18T10:04'),
  exporterBUYN: 5786,
  fraisObligatoiresPayesYN: 5887,
  datePaiementFraisObligatoires: dayjs('2024-05-17T20:11'),
  numeroQuitusCROUS: 10622,
  carteEturemiseYN: 17197,
  carteDupremiseYN: 10585,
  dateRemiseCarteEtu: dayjs('2024-05-18T04:15'),
  dateRemiseCarteDup: 12978,
  inscritAdministrativementYN: 1562,
  dateInscriptionAdministrative: dayjs('2024-05-18T03:27'),
  derniereModification: dayjs('2024-05-18T11:41'),
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
