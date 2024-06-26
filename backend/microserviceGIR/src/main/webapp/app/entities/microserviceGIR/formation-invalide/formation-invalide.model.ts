import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';

export interface IFormationInvalide {
  id: number;
  actifYN?: boolean | null;
  formation?: Pick<IFormation, 'id'> | null;
  anneeAcademique?: Pick<IAnneeAcademique, 'id'> | null;
}

export type NewFormationInvalide = Omit<IFormationInvalide, 'id'> & { id: null };
