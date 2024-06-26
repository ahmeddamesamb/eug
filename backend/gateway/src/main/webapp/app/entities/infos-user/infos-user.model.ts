import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IHistoriqueConnexion } from 'app/entities/historique-connexion/historique-connexion.model';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { IInfoUserRessource } from 'app/entities/info-user-ressource/info-user-ressource.model';

export interface IInfosUser {
  id: number;
  dateAjout?: dayjs.Dayjs | null;
  actifYN?: boolean | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  historiqueConnexions?: Pick<IHistoriqueConnexion, 'id'>[] | null;
  userProfiles?: Pick<IUserProfile, 'id'>[] | null;
  infoUserRessources?: Pick<IInfoUserRessource, 'id'>[] | null;
}

export type NewInfosUser = Omit<IInfosUser, 'id'> & { id: null };
