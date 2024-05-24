import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';

export interface IFormationInvalide {
  id: number;
  actifYN?: number | null;
  formation?: Pick<IFormation, 'id'> | null;
  anneAcademique?: Pick<IAnneeAcademique, 'id'> | null;
}

export type NewFormationInvalide = Omit<IFormationInvalide, 'id'> & { id: null };
