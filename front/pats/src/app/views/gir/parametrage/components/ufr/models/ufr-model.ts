import {UniversiteModel} from '../../universite/models/universite-model';

export interface UfrModel {
    id?:number,
    libelleUfr?:string,
    sigleUfr?:string,
    prefixe?:string,
    actifYN?:boolean,
    universite?: UniversiteModel,
}