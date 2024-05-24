import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface IFormationPrivee {
  id: number;
  nombreMensualites?: number | null;
  paiementPremierMoisYN?: number | null;
  paiementDernierMoisYN?: number | null;
  fraisDossierYN?: number | null;
  coutTotal?: number | null;
  mensualite?: number | null;
  formation?: Pick<IFormation, 'id'> | null;
}

export type NewFormationPrivee = Omit<IFormationPrivee, 'id'> & { id: null };
