import { EtudiantModel } from './etudiant-model'
import {TypeHandicapModel} from 'src/app/views/gir/parametrage/components/type-handicap/models/type-handicap-model'
import {TypeBourseModel} from 'src/app/views/gir/parametrage/components/type-bourse/models/type-bourse-model'

export interface InformationPersonellesModel {
    nomEtu?: string | null;
    nomJeuneFilleEtu?: string | null;
    prenomEtu?: string | null;
    statutMarital?: string | null;
    regime?: number | null;
    profession?: string | null;
    adresseEtu?: string | null;
    telEtu?: string | null;
    emailEtu?: string | null;
    adresseParent?: string | null;
    telParent?: string | null;
    emailParent?: string | null;
    nomParent?: string | null;
    prenomParent?: string | null;
    handicapYN?: number | null;
    photo?: string | null;
    ordiPersoYN?: number | null;
    derniereModification?: string | null;
    emailUser?: string | null;
    etudiant?: EtudiantModel;
    typeHandique?: TypeHandicapModel | null;
    typeBourse?: TypeBourseModel | null; 
}
