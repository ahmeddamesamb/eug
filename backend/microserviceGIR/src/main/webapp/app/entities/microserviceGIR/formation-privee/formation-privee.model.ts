import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';

export interface IFormationPrivee {
  id: number;
  nombreMensualites?: number | null;
  paiementPremierMoisYN?: boolean | null;
  paiementDernierMoisYN?: boolean | null;
  fraisDossierYN?: boolean | null;
  coutTotal?: number | null;
  mensualite?: number | null;
  actifYN?: boolean | null;
  formation?: Pick<IFormation, 'id'> | null;
}

export type NewFormationPrivee = Omit<IFormationPrivee, 'id'> & { id: null };
