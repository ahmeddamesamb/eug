import { IDomaine } from 'app/entities/microserviceGIR/domaine/domaine.model';
import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';

export interface IMention {
  id: number;
  libelleMention?: string | null;
  actifYN?: boolean | null;
  domaine?: Pick<IDomaine, 'id'> | null;
  specialites?: Pick<ISpecialite, 'id'>[] | null;
}

export type NewMention = Omit<IMention, 'id'> & { id: null };
