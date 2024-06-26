import { IDisciplineSportiveEtudiant } from 'app/entities/microserviceGIR/discipline-sportive-etudiant/discipline-sportive-etudiant.model';

export interface IDisciplineSportive {
  id: number;
  libelleDisciplineSportive?: string | null;
  disciplineSportiveEtudiants?: Pick<IDisciplineSportiveEtudiant, 'id'>[] | null;
}

export type NewDisciplineSportive = Omit<IDisciplineSportive, 'id'> & { id: null };
