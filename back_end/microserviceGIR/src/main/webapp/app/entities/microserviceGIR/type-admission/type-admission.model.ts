export interface ITypeAdmission {
  id: number;
  libelleTypeAdmission?: string | null;
}

export type NewTypeAdmission = Omit<ITypeAdmission, 'id'> & { id: null };
