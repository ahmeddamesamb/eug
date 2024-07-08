package sn.ugb.gir.service.dto;


import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationsDerniersInscriptionsDTO {

    private InscriptionAdministrativeFormation inscriptionAdministrativeFormation;
    private InformationPersonnelle informationPersonnelle;

    public InformationsDerniersInscriptionsDTO(InscriptionAdministrativeFormation inscriptionAdministrativeFormation, InformationPersonnelle informationPersonnelle) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
        this.informationPersonnelle = informationPersonnelle;
    }

    public InformationsDerniersInscriptionsDTO() {
    }

    public InscriptionAdministrativeFormation getInscriptionAdministrativeFormation() {
        return inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    public InformationPersonnelle getInformationPersonnelle() {
        return informationPersonnelle;
    }

    public void setInformationPersonnelle(InformationPersonnelle informationPersonnelle) {
        this.informationPersonnelle = informationPersonnelle;
    }

    @Override
    public String toString() {
        return "InformationsDerniersInscriptionsDTO{" +
            "inscriptionAdministrativeFormation=" + inscriptionAdministrativeFormation +
            ", informationPersonnelle=" + informationPersonnelle +
            '}';
    }
}
