import dayjs from 'dayjs/esm';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface IFormationValide {
  id: number;
  valideYN?: number | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  formation?: Pick<IFormation, 'id'> | null;
}

export type NewFormationValide = Omit<IFormationValide, 'id'> & { id: null };
