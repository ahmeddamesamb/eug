export interface IInformationImage {
  id: number;
  description?: string | null;
  cheminPath?: string | null;
  cheminFile?: string | null;
  enCoursYN?: string | null;
}

export type NewInformationImage = Omit<IInformationImage, 'id'> & { id: null };
