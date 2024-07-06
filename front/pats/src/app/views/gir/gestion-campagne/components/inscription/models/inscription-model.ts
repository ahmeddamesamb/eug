import { AnneeAcademiqueModel } from "src/app/views/gir/parametrage/components/annee-academique/models/AnneeAcademiqueModel";
import { FormationModel } from "src/app/views/gir/parametrage/components/formation/models/formation-model";
import { CampagneModel } from "../../campagnes/models/campagne-model";

export interface InscriptionModel {
  id?: number;
  libelleProgrammation: string;
  dateDebutProgrammation: string;
  dateFinProgrammation: string;
  ouvertYN: boolean;
  emailUser: string;
  dateForclosClasse: string;
  forclosClasseYN: boolean;
  actifYN: boolean;
  anneeAcademique: {
    id: number;
  };
  formation: {
    id: number;
  };
  campagne: {
    id: number;
  };
}
  
 