import dayjs from 'dayjs/esm';
import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { IRessource } from 'app/entities/ressource/ressource.model';

export interface IInfoUserRessource {
  id: number;
  dateAjout?: dayjs.Dayjs | null;
  enCoursYN?: boolean | null;
  infosUser?: Pick<IInfosUser, 'id'> | null;
  ressource?: Pick<IRessource, 'id'> | null;
}

export type NewInfoUserRessource = Omit<IInfoUserRessource, 'id'> & { id: null };
