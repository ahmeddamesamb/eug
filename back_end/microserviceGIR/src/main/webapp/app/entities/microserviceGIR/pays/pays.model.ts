import { IZone } from 'app/entities/microserviceGIR/zone/zone.model';

export interface IPays {
  id: number;
  libellePays?: string | null;
  paysEnAnglais?: string | null;
  nationalite?: string | null;
  codePays?: string | null;
  uEMOAYN?: number | null;
  cEDEAOYN?: number | null;
  rIMYN?: number | null;
  autreYN?: number | null;
  estEtrangerYN?: number | null;
  zones?: Pick<IZone, 'id'>[] | null;
}

export type NewPays = Omit<IPays, 'id'> & { id: null };
