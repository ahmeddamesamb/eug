import {DomaineModel} from '../../domaine/models/domaine-model';

export interface MentionModel {
        id?:number,
        libelleMention?:string,
        domaine?: DomaineModel ,
}
