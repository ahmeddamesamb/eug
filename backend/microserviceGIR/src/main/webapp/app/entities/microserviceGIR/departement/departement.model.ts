import { IUfr } from 'app/entities/microserviceGIR/ufr/ufr.model';

export interface IDepartement {
  id: number;
  nomDepatement?: string | null;
  actifYN?: boolean | null;
  ufr?: Pick<IUfr, 'id'> | null;
}

export type NewDepartement = Omit<IDepartement, 'id'> & { id: null };
