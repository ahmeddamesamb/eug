import dayjs from 'dayjs/esm';

export interface IRapport {
  id: number;
  libelleRapport?: string | null;
  descriptionRapport?: string | null;
  contenuRapport?: string | null;
  dateRedaction?: dayjs.Dayjs | null;
}

export type NewRapport = Omit<IRapport, 'id'> & { id: null };
