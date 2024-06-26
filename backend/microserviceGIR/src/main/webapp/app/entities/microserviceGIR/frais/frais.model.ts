import dayjs from 'dayjs/esm';
import { ITypeFrais } from 'app/entities/microserviceGIR/type-frais/type-frais.model';
import { ICycle } from 'app/entities/microserviceGIR/cycle/cycle.model';
import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { IPaiementFrais } from 'app/entities/microserviceGIR/paiement-frais/paiement-frais.model';

export interface IFrais {
  id: number;
  valeurFrais?: number | null;
  descriptionFrais?: string | null;
  fraisPourAssimileYN?: boolean | null;
  fraisPourExonererYN?: boolean | null;
  dia?: number | null;
  dip?: number | null;
  fraisPrivee?: number | null;
  dateApplication?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  estEnApplicationYN?: boolean | null;
  actifYN?: boolean | null;
  typeFrais?: Pick<ITypeFrais, 'id'> | null;
  typeCycle?: Pick<ICycle, 'id'> | null;
  universites?: Pick<IUniversite, 'id'>[] | null;
  paiementFrais?: Pick<IPaiementFrais, 'id'>[] | null;
}

export type NewFrais = Omit<IFrais, 'id'> & { id: null };
