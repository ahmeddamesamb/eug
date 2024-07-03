import {SerieModel} from 'src/app/views/gir/parametrage/components/serie/models/serie-model'
export interface BaccalaureatModel {
    id: number;
    origineScolaire?: string | null;
    anneeBac?: Date | null;
    numeroTable?: number | null;
    natureBac?: string | null;
    mentionBac?: string | null;
    moyenneSelectionBac?: number | null;
    moyenneBac?: number | null;
    actifYN?: boolean | null;
    serie?: SerieModel | null;
}
