import dayjs from 'dayjs/esm';
import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';

export interface IMinistere {
  id: number;
  nomMinistere?: string | null;
  sigleMinistere?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  enCoursYN?: boolean | null;
  actifYN?: boolean | null;
  universites?: Pick<IUniversite, 'id'>[] | null;
}

export type NewMinistere = Omit<IMinistere, 'id'> & { id: null };
