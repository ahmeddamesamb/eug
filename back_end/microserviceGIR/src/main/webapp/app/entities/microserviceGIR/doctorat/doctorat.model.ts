import dayjs from 'dayjs/esm';

export interface IDoctorat {
  id: number;
  sujet?: string | null;
  anneeInscriptionDoctorat?: dayjs.Dayjs | null;
  encadreurId?: number | null;
  laboratoirId?: number | null;
}

export type NewDoctorat = Omit<IDoctorat, 'id'> & { id: null };
