import dayjs from 'dayjs/esm';
import { IInscriptionDoctorat } from 'app/entities/microserviceGIR/inscription-doctorat/inscription-doctorat.model';

export interface IDoctorat {
  id: number;
  sujet?: string | null;
  anneeInscriptionDoctorat?: dayjs.Dayjs | null;
  encadreurId?: number | null;
  laboratoirId?: number | null;
  inscriptionDoctorats?: Pick<IInscriptionDoctorat, 'id'>[] | null;
}

export type NewDoctorat = Omit<IDoctorat, 'id'> & { id: null };
