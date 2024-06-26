import { IInformationPersonnelle } from 'app/entities/microserviceGIR/information-personnelle/information-personnelle.model';

export interface ITypeHandicap {
  id: number;
  libelleTypeHandicap?: string | null;
  informationPersonnelles?: Pick<IInformationPersonnelle, 'id'>[] | null;
}

export type NewTypeHandicap = Omit<ITypeHandicap, 'id'> & { id: null };
