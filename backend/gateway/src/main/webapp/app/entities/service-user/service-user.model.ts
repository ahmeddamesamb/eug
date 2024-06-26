import dayjs from 'dayjs/esm';
import { IBlocFonctionnel } from 'app/entities/bloc-fonctionnel/bloc-fonctionnel.model';

export interface IServiceUser {
  id: number;
  nom?: string | null;
  dateAjout?: dayjs.Dayjs | null;
  actifYN?: boolean | null;
  blocFonctionnels?: Pick<IBlocFonctionnel, 'id'>[] | null;
}

export type NewServiceUser = Omit<IServiceUser, 'id'> & { id: null };
