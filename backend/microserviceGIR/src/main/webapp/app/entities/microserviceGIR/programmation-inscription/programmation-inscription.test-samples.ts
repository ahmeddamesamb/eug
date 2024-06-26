import dayjs from 'dayjs/esm';

import { IProgrammationInscription, NewProgrammationInscription } from './programmation-inscription.model';

export const sampleWithRequiredData: IProgrammationInscription = {
  id: 18988,
};

export const sampleWithPartialData: IProgrammationInscription = {
  id: 791,
  dateDebutProgrammation: dayjs('2024-06-25'),
  emailUser: 'au cas où équipe de recherche',
  actifYN: true,
};

export const sampleWithFullData: IProgrammationInscription = {
  id: 7169,
  libelleProgrammation: 'quand espiègle',
  dateDebutProgrammation: dayjs('2024-06-25'),
  dateFinProgrammation: dayjs('2024-06-25'),
  ouvertYN: false,
  emailUser: 'électorat',
  dateForclosClasse: dayjs('2024-06-25'),
  forclosClasseYN: true,
  actifYN: false,
};

export const sampleWithNewData: NewProgrammationInscription = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
