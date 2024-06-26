import dayjs from 'dayjs/esm';

import { IBlocFonctionnel, NewBlocFonctionnel } from './bloc-fonctionnel.model';

export const sampleWithRequiredData: IBlocFonctionnel = {
  id: 13650,
  libelleBloc: 'trop',
  dateAjoutBloc: dayjs('2024-06-25'),
  actifYN: false,
};

export const sampleWithPartialData: IBlocFonctionnel = {
  id: 23954,
  libelleBloc: 'calme',
  dateAjoutBloc: dayjs('2024-06-25'),
  actifYN: true,
};

export const sampleWithFullData: IBlocFonctionnel = {
  id: 5429,
  libelleBloc: 'concurrence rejoindre',
  dateAjoutBloc: dayjs('2024-06-25'),
  actifYN: true,
};

export const sampleWithNewData: NewBlocFonctionnel = {
  libelleBloc: 'cuicui tic-tac',
  dateAjoutBloc: dayjs('2024-06-24'),
  actifYN: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
