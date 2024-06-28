import {PaysModel} from '../../pays/models/pays-model';

export interface RegionModel {
    id?:number,
    libelleRegion?:string,
    pays?: PaysModel,
}
