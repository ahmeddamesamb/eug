import dayjs from 'dayjs/esm';
import { IProgrammationInscription } from 'app/entities/microserviceGIR/programmation-inscription/programmation-inscription.model';

export interface ICampagne {
  id: number;
  libelleCampagne?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  libelleAbrege?: string | null;
  actifYN?: boolean | null;
  programmationInscriptions?: Pick<IProgrammationInscription, 'id'>[] | null;
}

export type NewCampagne = Omit<ICampagne, 'id'> & { id: null };
