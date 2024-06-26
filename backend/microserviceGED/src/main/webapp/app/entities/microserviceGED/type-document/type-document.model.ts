import { IDocumentDelivre } from 'app/entities/microserviceGED/document-delivre/document-delivre.model';

export interface ITypeDocument {
  id: number;
  libelleTypeDocument?: string | null;
  documentsDelivres?: Pick<IDocumentDelivre, 'id'>[] | null;
}

export type NewTypeDocument = Omit<ITypeDocument, 'id'> & { id: null };
