import {UniversiteModel} from '../../universite/models/universite-model';

export interface UfrModel {
    id?:number,
    libeleUfr?:string,
    sigleUfr?:string,
    systemeLMDYN?:number,
    ordreStat?:number,
    universite?: UniversiteModel,
}