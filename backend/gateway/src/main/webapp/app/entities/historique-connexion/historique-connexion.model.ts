import dayjs from 'dayjs/esm';
import { IInfosUser } from 'app/entities/infos-user/infos-user.model';

export interface IHistoriqueConnexion {
  id: number;
  dateDebutConnexion?: dayjs.Dayjs | null;
  dateFinConnexion?: dayjs.Dayjs | null;
  adresseIp?: string | null;
  actifYN?: boolean | null;
  infoUser?: Pick<IInfosUser, 'id'> | null;
}

export type NewHistoriqueConnexion = Omit<IHistoriqueConnexion, 'id'> & { id: null };
