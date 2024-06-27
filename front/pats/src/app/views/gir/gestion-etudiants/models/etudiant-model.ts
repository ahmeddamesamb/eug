export interface EtudiantModel {
    id: number;
    codeEtu?: string | null;
    ine?: string | null;
    codeBU?: number | null;
    emailUGB?: string | null;
    dateNaissEtu?: Date | null;
    lieuNaissEtu?: string | null;
    sexe?: string | null;
    numDocIdentite?: string | null;
    assimileYN?: number | null;
    exonereYN?: number | null;
    /* region?: Pick<IRegion, 'id'> | null;
    typeSelection?: Pick<ITypeSelection, 'id'> | null;
    lycee?: Pick<ILycee, 'id'> | null;
    informationPersonnelle?: Pick<IInformationPersonnelle, 'id'> | null;
    baccalaureat?: Pick<IBaccalaureat, 'id'> | null; */

    //les données sur la table information

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
    /* typeHandique?: Pick<ITypeHandicap, 'id'> | null;
    typeBourse?: Pick<ITypeBourse, 'id'> | null; */
}
