import { IDomaine } from 'app/entities/microserviceGIR/domaine/domaine.model';

export interface IMention {
  id: number;
  libelleMention?: string | null;
  domaine?: Pick<IDomaine, 'id'> | null;
}

export type NewMention = Omit<IMention, 'id'> & { id: null };
