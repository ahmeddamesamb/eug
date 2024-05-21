import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 5135,
};

export const sampleWithPartialData: IUtilisateur = {
  id: 25963,
  nom: 'superintend whenever so',
  email: 'Ward30@hotmail.com',
  motDePasse: 'easy-going pseudoscience ha',
  departement: 'following',
};

export const sampleWithFullData: IUtilisateur = {
  id: 2304,
  nom: 'hometown',
  prenom: 'till overcook',
  email: 'Layne9@yahoo.com',
  motDePasse: 'aw whoever',
  role: 'delectable',
  matricule: 'snoopy',
  departement: 'endothelium well',
  statut: 'intrude terrible brr',
};

export const sampleWithNewData: NewUtilisateur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
