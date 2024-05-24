import dayjs from 'dayjs/esm';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { ITypeHandicap } from 'app/entities/microserviceGIR/type-handicap/type-handicap.model';
import { ITypeBourse } from 'app/entities/microserviceGIR/type-bourse/type-bourse.model';

export interface IInformationPersonnelle {
  id: number;
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
  derniereModification?: dayjs.Dayjs | null;
  emailUser?: string | null;
  etudiant?: Pick<IEtudiant, 'id'> | null;
  typeHandique?: Pick<ITypeHandicap, 'id'> | null;
  typeBourse?: Pick<ITypeBourse, 'id'> | null;
}

export type NewInformationPersonnelle = Omit<IInformationPersonnelle, 'id'> & { id: null };
