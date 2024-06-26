import dayjs from 'dayjs/esm';
import { IServiceUser } from 'app/entities/service-user/service-user.model';
import { IUserProfileBlocFonctionnel } from 'app/entities/user-profile-bloc-fonctionnel/user-profile-bloc-fonctionnel.model';

export interface IBlocFonctionnel {
  id: number;
  libelleBloc?: string | null;
  dateAjoutBloc?: dayjs.Dayjs | null;
  actifYN?: boolean | null;
  service?: Pick<IServiceUser, 'id'> | null;
  userProfileBlocFonctionnels?: Pick<IUserProfileBlocFonctionnel, 'id'>[] | null;
}

export type NewBlocFonctionnel = Omit<IBlocFonctionnel, 'id'> & { id: null };
