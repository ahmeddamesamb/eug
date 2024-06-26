import { ITypeFormation } from 'app/entities/microserviceGIR/type-formation/type-formation.model';
import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';
import { IDepartement } from 'app/entities/microserviceGIR/departement/departement.model';
import { IFormationPrivee } from 'app/entities/microserviceGIR/formation-privee/formation-privee.model';
import { IFormationInvalide } from 'app/entities/microserviceGIR/formation-invalide/formation-invalide.model';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { IProgrammationInscription } from 'app/entities/microserviceGIR/programmation-inscription/programmation-inscription.model';
import { IFormationAutorisee } from 'app/entities/microserviceGIR/formation-autorisee/formation-autorisee.model';

export interface IFormation {
  id: number;
  classeDiplomanteYN?: boolean | null;
  libelleDiplome?: string | null;
  codeFormation?: string | null;
  nbreCreditsMin?: number | null;
  estParcoursYN?: boolean | null;
  lmdYN?: boolean | null;
  actifYN?: boolean | null;
  typeFormation?: Pick<ITypeFormation, 'id'> | null;
  niveau?: Pick<INiveau, 'id'> | null;
  specialite?: Pick<ISpecialite, 'id'> | null;
  departement?: Pick<IDepartement, 'id'> | null;
  formationPrivee?: Pick<IFormationPrivee, 'id'> | null;
  formationInvalides?: Pick<IFormationInvalide, 'id'>[] | null;
  inscriptionAdministrativeFormations?: Pick<IInscriptionAdministrativeFormation, 'id'>[] | null;
  programmationInscriptions?: Pick<IProgrammationInscription, 'id'>[] | null;
  formationAutorisees?: Pick<IFormationAutorisee, 'id'>[] | null;
}

export type NewFormation = Omit<IFormation, 'id'> & { id: null };
