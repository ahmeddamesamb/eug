import dayjs from 'dayjs/esm';
import { IProfil } from 'app/entities/profil/profil.model';
import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { IUserProfileBlocFonctionnel } from 'app/entities/user-profile-bloc-fonctionnel/user-profile-bloc-fonctionnel.model';

export interface IUserProfile {
  id: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  enCoursYN?: boolean | null;
  profil?: Pick<IProfil, 'id'> | null;
  infoUser?: Pick<IInfosUser, 'id'> | null;
  userProfileBlocFonctionnels?: Pick<IUserProfileBlocFonctionnel, 'id'>[] | null;
}

export type NewUserProfile = Omit<IUserProfile, 'id'> & { id: null };
