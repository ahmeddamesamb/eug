import { IDomaine, NewDomaine } from './domaine.model';

export const sampleWithRequiredData: IDomaine = {
  id: 30461,
  libelleDomaine: 'adversaire enseigner briser',
};

export const sampleWithPartialData: IDomaine = {
  id: 21887,
  libelleDomaine: 'du côté de profiter',
};

export const sampleWithFullData: IDomaine = {
  id: 27063,
  libelleDomaine: 'étant donné que dedans',
  actifYN: true,
};

export const sampleWithNewData: NewDomaine = {
  libelleDomaine: 'entamer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
