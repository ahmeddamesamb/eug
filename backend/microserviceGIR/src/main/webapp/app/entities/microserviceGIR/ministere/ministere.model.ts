import dayjs from 'dayjs/esm';

export interface IMinistere {
  id: number;
  nomMinistere?: string | null;
  sigleMinistere?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  enCours?: number | null;
}

export type NewMinistere = Omit<IMinistere, 'id'> & { id: null };
