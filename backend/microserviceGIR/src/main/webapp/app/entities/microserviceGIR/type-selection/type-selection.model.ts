import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';

export interface ITypeSelection {
  id: number;
  libelleTypeSelection?: string | null;
  etudiants?: Pick<IEtudiant, 'id'>[] | null;
}

export type NewTypeSelection = Omit<ITypeSelection, 'id'> & { id: null };
