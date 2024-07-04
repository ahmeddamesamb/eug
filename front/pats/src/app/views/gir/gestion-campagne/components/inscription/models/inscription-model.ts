import {CampagneModel} from '../../campagnes/models/campagne-model';

export interface InscriptionModel{
    id: number,
    libelle: string,
    dateDebut: Date | string,
    dateFin: Date | string,
    dateForclos: Date | string,
    campagne: CampagneModel | string,
    formation: string

}