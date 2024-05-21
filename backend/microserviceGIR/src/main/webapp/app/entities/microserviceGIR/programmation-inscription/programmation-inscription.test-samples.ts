import dayjs from 'dayjs/esm';

import { IProgrammationInscription, NewProgrammationInscription } from './programmation-inscription.model';

export const sampleWithRequiredData: IProgrammationInscription = {
  id: 14557,
};

export const sampleWithPartialData: IProgrammationInscription = {
  id: 23283,
  libelleProgrammation: 'drat provided offset',
  dateDebut: dayjs('2024-05-18'),
  emailUser: 'vivaciously',
};

export const sampleWithFullData: IProgrammationInscription = {
  id: 25481,
  libelleProgrammation: 'hm furthermore',
  dateDebut: dayjs('2024-05-18'),
  dateFin: dayjs('2024-05-18'),
  ouvertYN: 23383,
  emailUser: 'inasmuch',
};

export const sampleWithNewData: NewProgrammationInscription = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
