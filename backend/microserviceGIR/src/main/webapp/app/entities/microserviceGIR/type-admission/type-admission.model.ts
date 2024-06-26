import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';

export interface ITypeAdmission {
  id: number;
  libelleTypeAdmission?: string | null;
  inscriptionAdministratives?: Pick<IInscriptionAdministrative, 'id'>[] | null;
}

export type NewTypeAdmission = Omit<ITypeAdmission, 'id'> & { id: null };
