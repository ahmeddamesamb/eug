import { AnneeAcademiqueModel } from 'src/app/views/gir/parametrage/components/annee-academique/models/AnneeAcademiqueModel';
import {CampagneModel} from '../../campagnes/models/campagne-model';

export interface InscriptionModel{
    id: number,
    libelle: string,
    dateDebut: Date | string,
    dateFin: Date | string,
    dateForclos: Date | string,
    campagne: CampagneModel | string,
    formation: string,
    anneeAcademique: AnneeAcademiqueModel | string,

}