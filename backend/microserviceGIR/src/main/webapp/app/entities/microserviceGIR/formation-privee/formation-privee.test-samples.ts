import { IFormationPrivee, NewFormationPrivee } from './formation-privee.model';

export const sampleWithRequiredData: IFormationPrivee = {
  id: 26125,
  nombreMensualites: 1301,
  coutTotal: 12998.73,
  mensualite: 17076.18,
};

export const sampleWithPartialData: IFormationPrivee = {
  id: 19155,
  nombreMensualites: 7735,
  paiementPremierMoisYN: true,
  coutTotal: 25165.95,
  mensualite: 26020.79,
};

export const sampleWithFullData: IFormationPrivee = {
  id: 17014,
  nombreMensualites: 3115,
  paiementPremierMoisYN: false,
  paiementDernierMoisYN: false,
  fraisDossierYN: true,
  coutTotal: 6011.21,
  mensualite: 28639.91,
  actifYN: false,
};

export const sampleWithNewData: NewFormationPrivee = {
  nombreMensualites: 13246,
  coutTotal: 14518.38,
  mensualite: 24728.98,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
