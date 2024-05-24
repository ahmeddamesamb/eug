import { ITypeAdmission } from 'app/entities/microserviceGIR/type-admission/type-admission.model';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { IProcessusDinscriptionAdministrative } from 'app/entities/microserviceGIR/processus-dinscription-administrative/processus-dinscription-administrative.model';

export interface IInscriptionAdministrative {
  id: number;
  nouveauInscritYN?: number | null;
  repriseYN?: number | null;
  autoriseYN?: number | null;
  ordreInscription?: number | null;
  typeAdmission?: Pick<ITypeAdmission, 'id'> | null;
  anneeAcademique?: Pick<IAnneeAcademique, 'id'> | null;
  etudiant?: Pick<IEtudiant, 'id'> | null;
  processusDinscriptionAdministrative?: Pick<IProcessusDinscriptionAdministrative, 'id'> | null;
}

export type NewInscriptionAdministrative = Omit<IInscriptionAdministrative, 'id'> & { id: null };
