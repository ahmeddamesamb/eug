import dayjs from 'dayjs/esm';
import { ITypeFrais } from 'app/entities/microserviceGIR/type-frais/type-frais.model';
import { CYCLE } from 'app/entities/enumerations/cycle.model';

export interface IFrais {
  id: number;
  valeurFrais?: number | null;
  descriptionFrais?: string | null;
  fraispourAssimileYN?: number | null;
  cycle?: keyof typeof CYCLE | null;
  dia?: number | null;
  dip?: number | null;
  dipPrivee?: number | null;
  dateApplication?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  estEnApplicationYN?: number | null;
  typeFrais?: Pick<ITypeFrais, 'id'> | null;
}

export type NewFrais = Omit<IFrais, 'id'> & { id: null };
