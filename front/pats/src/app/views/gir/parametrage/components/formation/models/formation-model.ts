import {TypeformationModel} from '../../typeformation/models/typeformation-model';
import {NiveauModel} from '../../niveau/models/niveau-model';
import {SpecialiteModel} from '../../specialite/models/specialite-model';
import {DepartementModel} from '../../departement/models/departement-model';

export interface FormationModel {
    id?:number,
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
}
