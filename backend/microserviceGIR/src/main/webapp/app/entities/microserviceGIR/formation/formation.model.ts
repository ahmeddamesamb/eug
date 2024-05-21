import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';
import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { IFormationAutorisee } from 'app/entities/microserviceGIR/formation-autorisee/formation-autorisee.model';

export interface IFormation {
  id: number;
  nombreMensualites?: number | null;
  fraisDossierYN?: number | null;
  classeDiplomanteYN?: number | null;
  libelleDiplome?: string | null;
  codeFormation?: string | null;
  nbreCreditsMin?: number | null;
  estParcoursYN?: number | null;
  lmdYN?: number | null;
  specialite?: Pick<ISpecialite, 'id'> | null;
  niveau?: Pick<INiveau, 'id'> | null;
  formationAutorisees?: Pick<IFormationAutorisee, 'id'>[] | null;
}

export type NewFormation = Omit<IFormation, 'id'> & { id: null };
