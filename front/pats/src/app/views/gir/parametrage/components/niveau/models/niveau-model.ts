import {CycleModel} from '../../cycle/models/cycle-model';

export interface NiveauModel {
    id?:number,
    libelleNiveau?:string,
    codeNiveau?:string,
    anneeEtude?:string,
    actifYN?: boolean,
    typeCycle?:CycleModel
}
