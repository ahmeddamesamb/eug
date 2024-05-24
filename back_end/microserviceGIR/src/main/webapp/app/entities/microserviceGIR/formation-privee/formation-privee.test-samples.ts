import { IFormationPrivee, NewFormationPrivee } from './formation-privee.model';

export const sampleWithRequiredData: IFormationPrivee = {
  id: 1921,
  nombreMensualites: 26125,
  coutTotal: 1301.29,
  mensualite: 12998.73,
};

export const sampleWithPartialData: IFormationPrivee = {
  id: 21420,
  nombreMensualites: 28450,
  paiementDernierMoisYN: 19155,
  coutTotal: 7735.09,
  mensualite: 1952.1,
};

export const sampleWithFullData: IFormationPrivee = {
  id: 25166,
  nombreMensualites: 26021,
  paiementPremierMoisYN: 17014,
  paiementDernierMoisYN: 3115,
  fraisDossierYN: 25233,
  coutTotal: 25196.74,
  mensualite: 344.82,
};

export const sampleWithNewData: NewFormationPrivee = {
  nombreMensualites: 6011,
  coutTotal: 28639.91,
  mensualite: 26657.2,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
