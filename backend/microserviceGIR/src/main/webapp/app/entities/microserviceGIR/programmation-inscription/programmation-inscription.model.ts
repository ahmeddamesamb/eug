import dayjs from 'dayjs/esm';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { ICampagne } from 'app/entities/microserviceGIR/campagne/campagne.model';

export interface IProgrammationInscription {
  id: number;
  libelleProgrammation?: string | null;
  dateDebutProgrammation?: dayjs.Dayjs | null;
  dateFinProgrammation?: dayjs.Dayjs | null;
  ouvertYN?: boolean | null;
  emailUser?: string | null;
  dateForclosClasse?: dayjs.Dayjs | null;
  forclosClasseYN?: boolean | null;
  actifYN?: boolean | null;
  anneeAcademique?: Pick<IAnneeAcademique, 'id'> | null;
  formation?: Pick<IFormation, 'id'> | null;
  campagne?: Pick<ICampagne, 'id'> | null;
}

export type NewProgrammationInscription = Omit<IProgrammationInscription, 'id'> & { id: null };
