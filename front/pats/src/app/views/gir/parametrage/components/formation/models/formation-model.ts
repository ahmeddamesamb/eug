import {TypeformationModel} from '../../typeformation/models/typeformation-model';
import {NiveauModel} from '../../niveau/models/niveau-model';
import {SpecialiteModel} from '../../specialite/models/specialite-model';
import {DepartementModel} from '../../departement/models/departement-model';

export interface FormationModel {
    id?:number,
<<<<<<< HEAD
    libelleDiplome?:string,
=======
    classeDiplomanteYN?:boolean,
    libelleDiplome?:string,
    codeFormation?:string,
    nbreCreditsMin?:number,
    estParcoursYN?:boolean,
    lmdYN?:boolean,
    actifYN?:boolean,
    typeFormation?:TypeformationModel,
    niveau?:NiveauModel,
    specialite?:SpecialiteModel,
    departement?:DepartementModel  
>>>>>>> eb1fff07f315284ee072e4d730b46db6ad81917c
}
