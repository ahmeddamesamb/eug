import dayjs from 'dayjs/esm';

import { IHistoriqueConnexion, NewHistoriqueConnexion } from './historique-connexion.model';

export const sampleWithRequiredData: IHistoriqueConnexion = {
  id: 8057,
  dateDebutConnexion: dayjs('2024-06-25'),
  dateFinConnexion: dayjs('2024-06-25'),
  actifYN: true,
};

export const sampleWithPartialData: IHistoriqueConnexion = {
  id: 25306,
  dateDebutConnexion: dayjs('2024-06-25'),
  dateFinConnexion: dayjs('2024-06-25'),
  adresseIp: 'électorat',
  actifYN: false,
};

export const sampleWithFullData: IHistoriqueConnexion = {
  id: 19987,
  dateDebutConnexion: dayjs('2024-06-25'),
  dateFinConnexion: dayjs('2024-06-24'),
  adresseIp: 'ha ha vu que chut',
  actifYN: true,
};

export const sampleWithNewData: NewHistoriqueConnexion = {
  dateDebutConnexion: dayjs('2024-06-25'),
  dateFinConnexion: dayjs('2024-06-25'),
  actifYN: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
