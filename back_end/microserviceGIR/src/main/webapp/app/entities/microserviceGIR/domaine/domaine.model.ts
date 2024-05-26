import { IUfr } from 'app/entities/microserviceGIR/ufr/ufr.model';

export interface IDomaine {
  id: number;
  libelleDomaine?: string | null;
  ufrs?: Pick<IUfr, 'id'>[] | null;
}

export type NewDomaine = Omit<IDomaine, 'id'> & { id: null };
