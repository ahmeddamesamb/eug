import dayjs from 'dayjs/esm';
import { ITypeDocument } from 'app/entities/microserviceGED/type-document/type-document.model';

export interface IDocumentDelivre {
  id: number;
  libelleDoc?: string | null;
  anneeDoc?: dayjs.Dayjs | null;
  dateEnregistrement?: dayjs.Dayjs | null;
  typeDocument?: Pick<ITypeDocument, 'id'> | null;
}

export type NewDocumentDelivre = Omit<IDocumentDelivre, 'id'> & { id: null };
