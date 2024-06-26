import { IBaccalaureat } from 'app/entities/microserviceGIR/baccalaureat/baccalaureat.model';

export interface ISerie {
  id: number;
  codeSerie?: string | null;
  libelleSerie?: string | null;
  sigleSerie?: string | null;
  baccalaureats?: Pick<IBaccalaureat, 'id'>[] | null;
}

export type NewSerie = Omit<ISerie, 'id'> & { id: null };
