import { IInformationPersonnelle } from 'app/entities/microserviceGIR/information-personnelle/information-personnelle.model';

export interface ITypeBourse {
  id: number;
  libelleTypeBourse?: string | null;
  informationPersonnelles?: Pick<IInformationPersonnelle, 'id'>[] | null;
}

export type NewTypeBourse = Omit<ITypeBourse, 'id'> & { id: null };
