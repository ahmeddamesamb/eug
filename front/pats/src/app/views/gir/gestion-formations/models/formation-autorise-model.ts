import {FormationModel} from '../../parametrage/components/formation/models/formation-model';
export interface FormationAutoriseModel {
    id?:number,
    dateDebut?:Date,
    dateFin?:Date,
    enCoursYN?:boolean,
    actifYN?:boolean,
    formations?:FormationModel
}
