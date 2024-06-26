import dayjs from 'dayjs/esm';

export interface IPlanning {
  id: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
}

export type NewPlanning = Omit<IPlanning, 'id'> & { id: null };
