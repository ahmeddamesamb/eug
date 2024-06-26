import { ITypeAdmission } from 'app/entities/microserviceGIR/type-admission/type-admission.model';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { IProcessusInscriptionAdministrative } from 'app/entities/microserviceGIR/processus-inscription-administrative/processus-inscription-administrative.model';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';

export interface IInscriptionAdministrative {
  id: number;
  nouveauInscritYN?: boolean | null;
  repriseYN?: boolean | null;
  autoriseYN?: boolean | null;
  ordreInscription?: number | null;
  typeAdmission?: Pick<ITypeAdmission, 'id'> | null;
  anneeAcademique?: Pick<IAnneeAcademique, 'id'> | null;
  etudiant?: Pick<IEtudiant, 'id'> | null;
  processusDinscriptionAdministrative?: Pick<IProcessusInscriptionAdministrative, 'id'> | null;
  inscriptionAdministrativeFormations?: Pick<IInscriptionAdministrativeFormation, 'id'>[] | null;
}

export type NewInscriptionAdministrative = Omit<IInscriptionAdministrative, 'id'> & { id: null };
