import { IDisciplineSportive } from 'app/entities/microserviceGIR/discipline-sportive/discipline-sportive.model';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';

export interface IDisciplineSportiveEtudiant {
  id: number;
  licenceSportiveYN?: number | null;
  competitionUGBYN?: number | null;
  disciplineSportive?: Pick<IDisciplineSportive, 'id'> | null;
  etudiant?: Pick<IEtudiant, 'id'> | null;
}

export type NewDisciplineSportiveEtudiant = Omit<IDisciplineSportiveEtudiant, 'id'> & { id: null };
