import { EtudiantModel } from './etudiant-model'
import {TypeadmissionModel} from 'src/app/views/gir/parametrage/components/typeadmission/models/typeadmission-model'
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
    typeHandique?: TypeadmissionModel | null;
    typeBourse?: TypeBourseModel | null; 
}
