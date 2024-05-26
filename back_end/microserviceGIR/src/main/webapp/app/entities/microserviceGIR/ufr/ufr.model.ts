import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { IDomaine } from 'app/entities/microserviceGIR/domaine/domaine.model';

export interface IUfr {
  id: number;
  libeleUfr?: string | null;
  sigleUfr?: string | null;
  systemeLMDYN?: number | null;
  ordreStat?: number | null;
  universite?: Pick<IUniversite, 'id'> | null;
  domaines?: Pick<IDomaine, 'id'>[] | null;
}

export type NewUfr = Omit<IUfr, 'id'> & { id: null };
