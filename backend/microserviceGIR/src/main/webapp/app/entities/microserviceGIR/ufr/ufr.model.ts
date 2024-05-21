import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';

export interface IUFR {
  id: number;
  libeleUFR?: string | null;
  sigleUFR?: string | null;
  systemeLMDYN?: number | null;
  ordreStat?: number | null;
  universite?: Pick<IUniversite, 'id' | 'nomUniversite'> | null;
}

export type NewUFR = Omit<IUFR, 'id'> & { id: null };
