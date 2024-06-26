import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { IDomaine } from 'app/entities/microserviceGIR/domaine/domaine.model';

export interface IUfr {
  id: number;
  libelleUfr?: string | null;
  sigleUfr?: string | null;
  prefixe?: string | null;
  actifYN?: boolean | null;
  universite?: Pick<IUniversite, 'id'> | null;
  domaines?: Pick<IDomaine, 'id'>[] | null;
}

export type NewUfr = Omit<IUfr, 'id'> & { id: null };
