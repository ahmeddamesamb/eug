import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';
import { IFormationAutorisee } from 'app/entities/microserviceGIR/formation-autorisee/formation-autorisee.model';
import { IFormationPrivee } from 'app/entities/microserviceGIR/formation-privee/formation-privee.model';
import { TypeFormation } from 'app/entities/enumerations/type-formation.model';

export interface IFormation {
  id: number;
  fraisDossierYN?: number | null;
  classeDiplomanteYN?: number | null;
  libelleDiplome?: string | null;
  codeFormation?: string | null;
  nbreCreditsMin?: number | null;
  estParcoursYN?: number | null;
  lmdYN?: number | null;
  typeFormation?: keyof typeof TypeFormation | null;
  niveau?: Pick<INiveau, 'id'> | null;
  specialite?: Pick<ISpecialite, 'id'> | null;
  formationAutorisees?: Pick<IFormationAutorisee, 'id'>[] | null;
  formationPrivee?: Pick<IFormationPrivee, 'id'> | null;
}

export type NewFormation = Omit<IFormation, 'id'> & { id: null };
