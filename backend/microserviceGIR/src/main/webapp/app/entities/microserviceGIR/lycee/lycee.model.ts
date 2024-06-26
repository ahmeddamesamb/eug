import { IRegion } from 'app/entities/microserviceGIR/region/region.model';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';

export interface ILycee {
  id: number;
  nomLycee?: string | null;
  codeLycee?: string | null;
  villeLycee?: string | null;
  academieLycee?: number | null;
  centreExamen?: string | null;
  region?: Pick<IRegion, 'id'> | null;
  etudiants?: Pick<IEtudiant, 'id'>[] | null;
}

export type NewLycee = Omit<ILycee, 'id'> & { id: null };
