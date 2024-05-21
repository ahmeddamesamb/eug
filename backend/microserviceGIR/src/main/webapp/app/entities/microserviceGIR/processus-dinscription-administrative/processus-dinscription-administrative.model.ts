import dayjs from 'dayjs/esm';
import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';

export interface IProcessusDinscriptionAdministrative {
  id: number;
  inscriptionDemarreeYN?: number | null;
  dateDemarrageInscription?: dayjs.Dayjs | null;
  inscriptionAnnuleeYN?: number | null;
  dateAnnulationInscription?: dayjs.Dayjs | null;
  apteYN?: number | null;
  dateVisiteMedicale?: dayjs.Dayjs | null;
  beneficiaireCROUSYN?: number | null;
  dateRemiseQuitusCROUS?: dayjs.Dayjs | null;
  nouveauCodeBUYN?: number | null;
  quitusBUYN?: number | null;
  dateRemiseQuitusBU?: dayjs.Dayjs | null;
  exporterBUYN?: number | null;
  fraisObligatoiresPayesYN?: number | null;
  datePaiementFraisObligatoires?: dayjs.Dayjs | null;
  numeroQuitusCROUS?: number | null;
  carteEturemiseYN?: number | null;
  carteDupremiseYN?: number | null;
  dateRemiseCarteEtu?: dayjs.Dayjs | null;
  dateRemiseCarteDup?: number | null;
  inscritAdministrativementYN?: number | null;
  dateInscriptionAdministrative?: dayjs.Dayjs | null;
  derniereModification?: dayjs.Dayjs | null;
  inscritOnlineYN?: number | null;
  emailUser?: string | null;
  inscriptionAdministrative?: Pick<IInscriptionAdministrative, 'id'> | null;
}

export type NewProcessusDinscriptionAdministrative = Omit<IProcessusDinscriptionAdministrative, 'id'> & { id: null };
