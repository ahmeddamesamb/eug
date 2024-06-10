import { MinistereModel } from "../../ministere/models/ministere-model";
export interface UniversiteModel {
    id:number,
    nomUniversite:string,
    sigleUniversite:string,
    ministere: MinistereModel,
}

  