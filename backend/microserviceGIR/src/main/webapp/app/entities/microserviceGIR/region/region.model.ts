import { IPays } from 'app/entities/microserviceGIR/pays/pays.model';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { ILycee } from 'app/entities/microserviceGIR/lycee/lycee.model';

export interface IRegion {
  id: number;
  libelleRegion?: string | null;
  pays?: Pick<IPays, 'id'> | null;
  etudiants?: Pick<IEtudiant, 'id'>[] | null;
  lycees?: Pick<ILycee, 'id'>[] | null;
}

export type NewRegion = Omit<IRegion, 'id'> & { id: null };
