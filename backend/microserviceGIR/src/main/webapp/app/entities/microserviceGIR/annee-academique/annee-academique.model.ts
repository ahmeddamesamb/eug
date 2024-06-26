import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { IFormationInvalide } from 'app/entities/microserviceGIR/formation-invalide/formation-invalide.model';
import { IProgrammationInscription } from 'app/entities/microserviceGIR/programmation-inscription/programmation-inscription.model';

export interface IAnneeAcademique {
  id: number;
  libelleAnneeAcademique?: string | null;
  anneeAc?: number | null;
  separateur?: string | null;
  anneeCouranteYN?: boolean | null;
  inscriptionAdministratives?: Pick<IInscriptionAdministrative, 'id'>[] | null;
  formationInvalides?: Pick<IFormationInvalide, 'id'>[] | null;
  programmationInscriptions?: Pick<IProgrammationInscription, 'id'>[] | null;
}

export type NewAnneeAcademique = Omit<IAnneeAcademique, 'id'> & { id: null };
