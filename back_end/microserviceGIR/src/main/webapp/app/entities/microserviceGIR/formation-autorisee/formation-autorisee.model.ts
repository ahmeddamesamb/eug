import dayjs from 'dayjs/esm';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface IFormationAutorisee {
  id: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  enCoursYN?: number | null;
  formations?: Pick<IFormation, 'id'>[] | null;
}

export type NewFormationAutorisee = Omit<IFormationAutorisee, 'id'> & { id: null };
