import { IRegion } from 'app/entities/microserviceGIR/region/region.model';

export interface ILycee {
  id: number;
  nomLycee?: string | null;
  codeLycee?: string | null;
  villeLycee?: string | null;
  academieLycee?: number | null;
  centreExamen?: string | null;
  region?: Pick<IRegion, 'id'> | null;
}

export type NewLycee = Omit<ILycee, 'id'> & { id: null };
