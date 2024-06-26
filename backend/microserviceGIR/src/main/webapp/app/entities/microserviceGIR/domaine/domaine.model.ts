import { IUfr } from 'app/entities/microserviceGIR/ufr/ufr.model';
import { IMention } from 'app/entities/microserviceGIR/mention/mention.model';

export interface IDomaine {
  id: number;
  libelleDomaine?: string | null;
  actifYN?: boolean | null;
  ufrs?: Pick<IUfr, 'id'>[] | null;
  mentions?: Pick<IMention, 'id'>[] | null;
}

export type NewDomaine = Omit<IDomaine, 'id'> & { id: null };
