import {CampagneModel} from '../../campagnes/models/campagne-model';

export interface InscriptionModel{
    id: number,
    dateDebut: Date | string,
    dateFin: Date | string,
    campagne: CampagneModel | string,
    formation: string

}