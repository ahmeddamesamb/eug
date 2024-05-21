import { ILaboratoire, NewLaboratoire } from './laboratoire.model';

export const sampleWithRequiredData: ILaboratoire = {
  id: 14176,
  nomLaboratoire: 'short farewell',
  laboratoireCotutelleYN: 26537,
};

export const sampleWithPartialData: ILaboratoire = {
  id: 1147,
  nomLaboratoire: 'jubilant following',
  laboratoireCotutelleYN: 25146,
};

export const sampleWithFullData: ILaboratoire = {
  id: 12792,
  nomLaboratoire: 'entire debtor gamble',
  laboratoireCotutelleYN: 4793,
};

export const sampleWithNewData: NewLaboratoire = {
  nomLaboratoire: 'extremely',
  laboratoireCotutelleYN: 18188,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
