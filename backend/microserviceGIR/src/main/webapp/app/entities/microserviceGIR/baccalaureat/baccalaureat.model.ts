import dayjs from 'dayjs/esm';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { ISerie } from 'app/entities/microserviceGIR/serie/serie.model';

export interface IBaccalaureat {
  id: number;
  origineScolaire?: string | null;
  anneeBac?: dayjs.Dayjs | null;
  numeroTable?: number | null;
  natureBac?: string | null;
  mentionBac?: string | null;
  moyenneSelectionBac?: number | null;
  moyenneBac?: number | null;
  etudiant?: Pick<IEtudiant, 'id'> | null;
  serie?: Pick<ISerie, 'id'> | null;
}

export type NewBaccalaureat = Omit<IBaccalaureat, 'id'> & { id: null };
