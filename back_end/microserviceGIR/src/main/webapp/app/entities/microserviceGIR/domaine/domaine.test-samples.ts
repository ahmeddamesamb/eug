import { IDomaine, NewDomaine } from './domaine.model';

export const sampleWithRequiredData: IDomaine = {
  id: 888,
  libelleDomaine: 'until regarding admirable',
};

export const sampleWithPartialData: IDomaine = {
  id: 13314,
  libelleDomaine: 'because geez hm',
};

export const sampleWithFullData: IDomaine = {
  id: 13102,
  libelleDomaine: 'across woot',
};

export const sampleWithNewData: NewDomaine = {
  libelleDomaine: 'outside',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
