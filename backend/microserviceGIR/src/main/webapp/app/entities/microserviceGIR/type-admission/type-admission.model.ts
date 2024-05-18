export interface ITypeAdmission {
  id: number;
  libelle?: string | null;
}

export type NewTypeAdmission = Omit<ITypeAdmission, 'id'> & { id: null };
