import { IDomaine, NewDomaine } from './domaine.model';

export const sampleWithRequiredData: IDomaine = {
  id: 888,
};

export const sampleWithPartialData: IDomaine = {
  id: 27114,
};

export const sampleWithFullData: IDomaine = {
  id: 28332,
  libelleDomaine: 'reproachfully',
};

export const sampleWithNewData: NewDomaine = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
