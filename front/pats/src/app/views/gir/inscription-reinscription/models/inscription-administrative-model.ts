import {AnneeAcademiqueModel} from '../../parametrage/components/annee-academique/models/AnneeAcademiqueModel';
import {TypeadmissionModel} from '../../parametrage/components/typeadmission/models/typeadmission-model';
import {EtudiantModel} from '../../gestion-etudiants/models/etudiant-model';

export interface InscriptionAdministrativeModel {
    id: number;
  nouveauInscritYN?: boolean | null;
  repriseYN?: boolean | null;
  autoriseYN?: boolean | null;
  ordreInscription?: number | null;
  typeAdmission?: TypeadmissionModel;
  anneeAcademique?: AnneeAcademiqueModel;
  etudiant?: EtudiantModel
}
