import {UfrModel} from '../../ufr/models/ufr-model'
export interface DepartementModel {
    id?:number,
    nomDepatement?:string,
    actifYN?:boolean,
    ufr?: UfrModel ,
}
