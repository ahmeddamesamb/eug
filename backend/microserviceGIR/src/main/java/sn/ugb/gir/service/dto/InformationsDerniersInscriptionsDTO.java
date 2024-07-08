package sn.ugb.gir.service.dto;


import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
/**
 * A DTO for the {@link sn.ugb.gir.domain.InformationsDerniersInscriptions} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationsDerniersInscriptionsDTO {

    private InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation;
    private InformationPersonnelleDTO informationPersonnelle;

    public InformationsDerniersInscriptionsDTO(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation, InformationPersonnelleDTO informationPersonnelle) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
        this.informationPersonnelle = informationPersonnelle;
    }

    public InformationsDerniersInscriptionsDTO() {
    }

    public InscriptionAdministrativeFormationDTO getInscriptionAdministrativeFormation() {
        return inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    public InformationPersonnelleDTO getInformationPersonnelle() {
        return informationPersonnelle;
    }

    public void setInformationPersonnelle(InformationPersonnelleDTO informationPersonnelle) {
        this.informationPersonnelle = informationPersonnelle;
    }

    @Override
    public String toString() {
        return "InformationsDerniersInscriptionsDTO{" +
            "inscriptionAdministrativeFormationDTO=" + inscriptionAdministrativeFormation +
            ", informationPersonnelleDTO=" + informationPersonnelle +
            '}';
    }
}
