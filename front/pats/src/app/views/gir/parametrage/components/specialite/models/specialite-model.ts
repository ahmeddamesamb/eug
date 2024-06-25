import {MentionModel} from '../../mention/models/mention-model';
export interface SpecialiteModel {
    id?:number,
    nomSpecialites?:string,
    sigleSpecialites?:string,
    specialiteParticulierYN?:number,
    specialitesPayanteYN?:number,
    mention?: MentionModel,
}
