import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 5135,
};

export const sampleWithPartialData: IUtilisateur = {
  id: 25963,
  nomUser: 'superintend whenever so',
  emailUser: 'what minor under',
  motDePasse: 'curiously blah page',
  departement: 'correlate twitter',
};

export const sampleWithFullData: IUtilisateur = {
  id: 19177,
  nomUser: 'warmly dancer',
  prenomUser: 'thankfully between snoopy',
  emailUser: 'endothelium well',
  motDePasse: 'intrude terrible brr',
  role: 'ouch um er',
  matricule: 'queasily encirclement',
  departement: 'flinch reliability',
  statut: 'evaporation before inside',
};

export const sampleWithNewData: NewUtilisateur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
