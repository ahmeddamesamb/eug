import { EtudiantModel } from './etudiant-model'
import {TypeHandicapModel} from 'src/app/views/gir/parametrage/components/type-handicap/models/type-handicap-model'
import {TypeBourseModel} from 'src/app/views/gir/parametrage/components/type-bourse/models/type-bourse-model'

export interface InformationPersonellesModel {
    id?: number;
    nomEtu?: string,
    nomJeuneFilleEtu?: string,
    prenomEtu?: string,
    statutMarital?: string,
    regime?: number,
    profession?: string,
    adresseEtu?: string,
    telEtu?: string,
    emailEtu?: string,
    adresseParent?: string,
    telParent?: string,
    emailParent?: string,
    nomParent?: string,
    prenomParent?: string,
    handicapYN?: boolean | null;
    photo?: string,
    ordiPersoYN?: boolean | null;
    derniereModification?: Date | string,
    emailUser?: string,
    etudiant?: EtudiantModel,
    typeHandique?: TypeHandicapModel,
    typeBourse?: TypeBourseModel 
}
