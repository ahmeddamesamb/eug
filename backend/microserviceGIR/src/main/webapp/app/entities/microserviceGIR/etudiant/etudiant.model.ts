import dayjs from 'dayjs/esm';
import { IRegion } from 'app/entities/microserviceGIR/region/region.model';
import { ITypeSelection } from 'app/entities/microserviceGIR/type-selection/type-selection.model';
import { ILycee } from 'app/entities/microserviceGIR/lycee/lycee.model';
import { IInformationPersonnelle } from 'app/entities/microserviceGIR/information-personnelle/information-personnelle.model';
import { IBaccalaureat } from 'app/entities/microserviceGIR/baccalaureat/baccalaureat.model';
import { IDisciplineSportiveEtudiant } from 'app/entities/microserviceGIR/discipline-sportive-etudiant/discipline-sportive-etudiant.model';
import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';

export interface IEtudiant {
  id: number;
  codeEtu?: string | null;
  ine?: string | null;
  codeBU?: string | null;
  emailUGB?: string | null;
  dateNaissEtu?: dayjs.Dayjs | null;
  lieuNaissEtu?: string | null;
  sexe?: string | null;
  numDocIdentite?: string | null;
  assimileYN?: boolean | null;
  actifYN?: boolean | null;
  region?: Pick<IRegion, 'id'> | null;
  typeSelection?: Pick<ITypeSelection, 'id'> | null;
  lycee?: Pick<ILycee, 'id'> | null;
  informationPersonnelle?: Pick<IInformationPersonnelle, 'id'> | null;
  baccalaureat?: Pick<IBaccalaureat, 'id'> | null;
  disciplineSportiveEtudiants?: Pick<IDisciplineSportiveEtudiant, 'id'>[] | null;
  inscriptionAdministratives?: Pick<IInscriptionAdministrative, 'id'>[] | null;
}

export type NewEtudiant = Omit<IEtudiant, 'id'> & { id: null };
