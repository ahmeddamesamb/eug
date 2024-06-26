import { IInfoUserRessource } from 'app/entities/info-user-ressource/info-user-ressource.model';

export interface IRessource {
  id: number;
  libelle?: string | null;
  actifYN?: boolean | null;
  infoUserRessources?: Pick<IInfoUserRessource, 'id'>[] | null;
}

export type NewRessource = Omit<IRessource, 'id'> & { id: null };
