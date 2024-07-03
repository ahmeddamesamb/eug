import {RegionModel} from 'src/app/views/gir/parametrage/components/region/models/region-model'
export interface LyceeModel {
    id: number;
    nomLycee?: string | null;
    codeLycee?: string | null;
    villeLycee?: string | null;
    academieLycee?: number | null;
    centreExamen?: string | null;
    region?: RegionModel | null;
}
