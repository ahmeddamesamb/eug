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
}
