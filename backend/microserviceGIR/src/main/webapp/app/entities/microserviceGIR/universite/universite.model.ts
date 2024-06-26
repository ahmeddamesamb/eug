import { IMinistere } from 'app/entities/microserviceGIR/ministere/ministere.model';
import { IUfr } from 'app/entities/microserviceGIR/ufr/ufr.model';
import { IFrais } from 'app/entities/microserviceGIR/frais/frais.model';

export interface IUniversite {
  id: number;
  nomUniversite?: string | null;
  sigleUniversite?: string | null;
  actifYN?: boolean | null;
  ministere?: Pick<IMinistere, 'id'> | null;
  ufrs?: Pick<IUfr, 'id'>[] | null;
  frais?: Pick<IFrais, 'id'>[] | null;
}

export type NewUniversite = Omit<IUniversite, 'id'> & { id: null };
