import dayjs from 'dayjs/esm';

export interface ICandidat {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  email?: string | null;
}

export type NewCandidat = Omit<ICandidat, 'id'> & { id: null };
