import dayjs from 'dayjs/esm';

export interface ICandidat {
  id: number;
  nomCanditat?: string | null;
  prenomCandidat?: string | null;
  dateNaissanceCandidat?: dayjs.Dayjs | null;
  emailCandidat?: string | null;
}

export type NewCandidat = Omit<ICandidat, 'id'> & { id: null };
