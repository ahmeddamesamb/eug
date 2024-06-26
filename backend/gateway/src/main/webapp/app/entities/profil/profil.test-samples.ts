import dayjs from 'dayjs/esm';

import { IProfil, NewProfil } from './profil.model';

export const sampleWithRequiredData: IProfil = {
  id: 26661,
  libelle: 'écarter assez',
  dateAjout: dayjs('2024-06-24'),
  actifYN: true,
};

export const sampleWithPartialData: IProfil = {
  id: 21607,
  libelle: 'rapide au prix de',
  dateAjout: dayjs('2024-06-25'),
  actifYN: false,
};

export const sampleWithFullData: IProfil = {
  id: 18241,
  libelle: 'en face de depuis parlementaire',
  dateAjout: dayjs('2024-06-25'),
  actifYN: true,
};

export const sampleWithNewData: NewProfil = {
  libelle: 'au lieu de quant à',
  dateAjout: dayjs('2024-06-25'),
  actifYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
