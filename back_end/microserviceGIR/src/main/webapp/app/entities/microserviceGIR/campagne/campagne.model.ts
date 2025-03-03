import dayjs from 'dayjs/esm';

export interface ICampagne {
  id: number;
  libelleCampagne?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  libelleAbrege?: string | null;
}

export type NewCampagne = Omit<ICampagne, 'id'> & { id: null };
