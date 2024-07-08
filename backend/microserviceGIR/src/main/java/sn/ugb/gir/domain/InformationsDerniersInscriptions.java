package sn.ugb.gir.domain;

public class InformationsDerniersInscriptions {

    private InscriptionAdministrativeFormation inscriptionAdministrativeFormation;
    private InformationPersonnelle informationPersonnelle;

    public InformationsDerniersInscriptions(InscriptionAdministrativeFormation inscriptionAdministrativeFormation, InformationPersonnelle informationPersonnelle) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
        this.informationPersonnelle = informationPersonnelle;
    }

    public InformationsDerniersInscriptions() {
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
