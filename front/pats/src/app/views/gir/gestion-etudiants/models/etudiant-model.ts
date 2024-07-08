import {RegionModel} from 'src/app/views/gir/parametrage/components/region/models/region-model'
import {TypeselectionModel} from 'src/app/views/gir/parametrage/components/typeselection/models/typeselection-model'
import {LyceeModel} from 'src/app/views/gir/parametrage/components/lycee/models/lycee-model'
import {BaccalaureatModel} from 'src/app/views/gir/parametrage/components/baccalaureat/models/baccalaureat-model'

export interface EtudiantModel {
    id?: number,
    codeEtu?: string | null;
    ine?: string | null;
    codeBU?: number | null;
    emailUGB?: string | null;
    dateNaissEtu?: Date | null;
    lieuNaissEtu?: string | null;
    sexe?: string | null;
    numDocIdentite?: string | null;
    assimileYN?: boolean | null;
    exonereYN?: boolean | null;
    actifYN?: boolean | null;
    region?: RegionModel | null;
    typeSelection?: TypeselectionModel | null;
    lycee?: LyceeModel | null;
    //baccalaureat?: BaccalaureatModel | null; 
}
