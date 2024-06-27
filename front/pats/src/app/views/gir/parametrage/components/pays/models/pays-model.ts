import {ZoneModel} from '../../zone/models/zone-model';


export interface PaysModel {
    id?:number,
    libellePays?:string,
    paysEnAnglais?:string,
    nationalite?:string,
    codePays?:string,
    getuEMOAYN?:number,
    getcEDEAOYN?:number,
    getrIMYN?:number,
    autreYN?:number,
    estEtrangerYN?:number,
    zone?: ZoneModel,
}
