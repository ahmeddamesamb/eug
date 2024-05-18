import dayjs from 'dayjs/esm';

import { IFormationValide, NewFormationValide } from './formation-valide.model';

export const sampleWithRequiredData: IFormationValide = {
  id: 9470,
};

export const sampleWithPartialData: IFormationValide = {
  id: 19237,
  valideYN: 14732,
  dateDebut: dayjs('2024-05-17'),
};

export const sampleWithFullData: IFormationValide = {
  id: 8884,
  valideYN: 15632,
  dateDebut: dayjs('2024-05-18'),
  dateFin: dayjs('2024-05-18'),
};

export const sampleWithNewData: NewFormationValide = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
