import dayjs from 'dayjs/esm';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';

export interface IProfil {
  id: number;
  libelle?: string | null;
  dateAjout?: dayjs.Dayjs | null;
  actifYN?: boolean | null;
  userProfiles?: Pick<IUserProfile, 'id'>[] | null;
}

export type NewProfil = Omit<IProfil, 'id'> & { id: null };
