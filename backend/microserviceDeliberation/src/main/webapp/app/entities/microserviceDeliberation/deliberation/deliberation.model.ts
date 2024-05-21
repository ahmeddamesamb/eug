export interface IDeliberation {
  id: number;
  estValidee?: number | null;
  pvDeliberation?: string | null;
  pvDeliberationContentType?: string | null;
}

export type NewDeliberation = Omit<IDeliberation, 'id'> & { id: null };
