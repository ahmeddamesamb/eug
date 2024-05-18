import dayjs from 'dayjs/esm';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { ICampagne } from 'app/entities/microserviceGIR/campagne/campagne.model';

export interface IProgrammationInscription {
  id: number;
  libelleProgrammation?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  ouvertYN?: number | null;
  emailUser?: string | null;
  anneeAcademique?: Pick<IAnneeAcademique, 'id'> | null;
  formation?: Pick<IFormation, 'id'> | null;
  campagne?: Pick<ICampagne, 'id'> | null;
}

export type NewProgrammationInscription = Omit<IProgrammationInscription, 'id'> & { id: null };
