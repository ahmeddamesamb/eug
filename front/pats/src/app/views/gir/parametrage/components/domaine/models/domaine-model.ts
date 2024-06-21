import {UfrModel} from '../../ufr/models/ufr-model';

export interface DomaineModel {
    id?:number,
    libelleDomaine?:string,
    ufrs?: UfrModel[],
}
