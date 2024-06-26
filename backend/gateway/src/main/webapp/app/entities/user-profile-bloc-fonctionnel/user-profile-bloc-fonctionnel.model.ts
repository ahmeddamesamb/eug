import dayjs from 'dayjs/esm';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { IBlocFonctionnel } from 'app/entities/bloc-fonctionnel/bloc-fonctionnel.model';

export interface IUserProfileBlocFonctionnel {
  id: number;
  date?: dayjs.Dayjs | null;
  enCoursYN?: boolean | null;
  userProfil?: Pick<IUserProfile, 'id'> | null;
  blocFonctionnel?: Pick<IBlocFonctionnel, 'id'> | null;
}

export type NewUserProfileBlocFonctionnel = Omit<IUserProfileBlocFonctionnel, 'id'> & { id: null };
