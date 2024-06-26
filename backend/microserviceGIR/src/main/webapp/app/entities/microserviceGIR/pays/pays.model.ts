import { IZone } from 'app/entities/microserviceGIR/zone/zone.model';
import { IRegion } from 'app/entities/microserviceGIR/region/region.model';

export interface IPays {
  id: number;
  libellePays?: string | null;
  paysEnAnglais?: string | null;
  nationalite?: string | null;
  codePays?: string | null;
  uEMOAYN?: boolean | null;
  cEDEAOYN?: boolean | null;
  rIMYN?: boolean | null;
  autreYN?: boolean | null;
  estEtrangerYN?: boolean | null;
  zones?: Pick<IZone, 'id'>[] | null;
  regions?: Pick<IRegion, 'id'>[] | null;
}

export type NewPays = Omit<IPays, 'id'> & { id: null };
