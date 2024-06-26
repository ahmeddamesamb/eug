import { IRessourceAide, NewRessourceAide } from './ressource-aide.model';

export const sampleWithRequiredData: IRessourceAide = {
  id: 7563,
  nom: 'pendant que',
};

export const sampleWithPartialData: IRessourceAide = {
  id: 9305,
  nom: 'de crainte que',
};

export const sampleWithFullData: IRessourceAide = {
  id: 1886,
  nom: 'si camarade hors',
  libelle: 'mélancolique',
};

export const sampleWithNewData: NewRessourceAide = {
  nom: 'croâ invoquer partenaire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
