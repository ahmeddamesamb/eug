import { IFrais } from 'app/entities/microserviceGIR/frais/frais.model';

export interface ITypeFrais {
  id: number;
  libelleTypeFrais?: string | null;
  frais?: Pick<IFrais, 'id'>[] | null;
}

export type NewTypeFrais = Omit<ITypeFrais, 'id'> & { id: null };
