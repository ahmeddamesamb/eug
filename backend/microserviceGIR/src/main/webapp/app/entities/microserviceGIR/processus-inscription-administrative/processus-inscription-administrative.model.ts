import dayjs from 'dayjs/esm';
import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';

export interface IProcessusInscriptionAdministrative {
  id: number;
  inscriptionDemarreeYN?: boolean | null;
  dateDemarrageInscription?: dayjs.Dayjs | null;
  inscriptionAnnuleeYN?: boolean | null;
  dateAnnulationInscription?: dayjs.Dayjs | null;
  apteYN?: boolean | null;
  dateVisiteMedicale?: dayjs.Dayjs | null;
  beneficiaireCROUSYN?: boolean | null;
  dateRemiseQuitusCROUS?: dayjs.Dayjs | null;
  nouveauCodeBUYN?: boolean | null;
  quitusBUYN?: boolean | null;
  dateRemiseQuitusBU?: dayjs.Dayjs | null;
  exporterBUYN?: boolean | null;
  fraisObligatoiresPayesYN?: boolean | null;
  datePaiementFraisObligatoires?: dayjs.Dayjs | null;
  numeroQuitusCROUS?: number | null;
  carteEturemiseYN?: boolean | null;
  carteDupremiseYN?: boolean | null;
  dateRemiseCarteEtu?: dayjs.Dayjs | null;
  dateRemiseCarteDup?: number | null;
  inscritAdministrativementYN?: boolean | null;
  dateInscriptionAdministrative?: dayjs.Dayjs | null;
  derniereModification?: dayjs.Dayjs | null;
  inscritOnlineYN?: boolean | null;
  emailUser?: string | null;
  inscriptionAdministrative?: Pick<IInscriptionAdministrative, 'id'> | null;
}

export type NewProcessusInscriptionAdministrative = Omit<IProcessusInscriptionAdministrative, 'id'> & { id: null };
