import dayjs from 'dayjs/esm';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface IFormationAutorisee {
  id: number;
  dateDebuT?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  enCours?: number | null;
  formations?: Pick<IFormation, 'id'>[] | null;
}

export type NewFormationAutorisee = Omit<IFormationAutorisee, 'id'> & { id: null };
