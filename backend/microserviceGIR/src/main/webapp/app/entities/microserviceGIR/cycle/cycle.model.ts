import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { IFrais } from 'app/entities/microserviceGIR/frais/frais.model';

export interface ICycle {
  id: number;
  libelleCycle?: string | null;
  niveaux?: Pick<INiveau, 'id'>[] | null;
  frais?: Pick<IFrais, 'id'>[] | null;
}

export type NewCycle = Omit<ICycle, 'id'> & { id: null };
