export interface ISerie {
  id: number;
  codeSerie?: string | null;
  libelleSerie?: string | null;
  sigleSerie?: string | null;
}

export type NewSerie = Omit<ISerie, 'id'> & { id: null };
