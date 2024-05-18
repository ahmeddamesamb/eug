export interface IDomaine {
  id: number;
  libelleDomaine?: string | null;
}

export type NewDomaine = Omit<IDomaine, 'id'> & { id: null };
