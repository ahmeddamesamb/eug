package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.InscriptionAdministrativeFormation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionAdministrativeFormationDTO implements Serializable {

    private Long id;

    private Boolean inscriptionPrincipaleYN;

    private Boolean inscriptionAnnuleeYN;

    @NotNull
    private Boolean exonereYN;

    private Boolean paiementFraisOblYN;

    private Boolean paiementFraisIntegergYN;

    private Boolean certificatDelivreYN;

    private Instant dateChoixFormation;

    private Instant dateValidationInscription;

    private InscriptionAdministrativeDTO inscriptionAdministrative;

    private FormationDTO formation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInscriptionPrincipaleYN() {
        return inscriptionPrincipaleYN;
    }

    public void setInscriptionPrincipaleYN(Boolean inscriptionPrincipaleYN) {
        this.inscriptionPrincipaleYN = inscriptionPrincipaleYN;
    }

    public Boolean getInscriptionAnnuleeYN() {
        return inscriptionAnnuleeYN;
    }

    public void setInscriptionAnnuleeYN(Boolean inscriptionAnnuleeYN) {
        this.inscriptionAnnuleeYN = inscriptionAnnuleeYN;
    }

    public Boolean getExonereYN() {
        return exonereYN;
    }

    public void setExonereYN(Boolean exonereYN) {
        this.exonereYN = exonereYN;
    }

    public Boolean getPaiementFraisOblYN() {
        return paiementFraisOblYN;
    }

    public void setPaiementFraisOblYN(Boolean paiementFraisOblYN) {
        this.paiementFraisOblYN = paiementFraisOblYN;
    }

    public Boolean getPaiementFraisIntegergYN() {
        return paiementFraisIntegergYN;
    }

    public void setPaiementFraisIntegergYN(Boolean paiementFraisIntegergYN) {
        this.paiementFraisIntegergYN = paiementFraisIntegergYN;
    }

    public Boolean getCertificatDelivreYN() {
        return certificatDelivreYN;
    }

    public void setCertificatDelivreYN(Boolean certificatDelivreYN) {
        this.certificatDelivreYN = certificatDelivreYN;
    }

    public Instant getDateChoixFormation() {
        return dateChoixFormation;
    }

    public void setDateChoixFormation(Instant dateChoixFormation) {
        this.dateChoixFormation = dateChoixFormation;
    }

    public Instant getDateValidationInscription() {
        return dateValidationInscription;
    }

    public void setDateValidationInscription(Instant dateValidationInscription) {
        this.dateValidationInscription = dateValidationInscription;
    }

    public InscriptionAdministrativeDTO getInscriptionAdministrative() {
        return inscriptionAdministrative;
    }

    public void setInscriptionAdministrative(InscriptionAdministrativeDTO inscriptionAdministrative) {
        this.inscriptionAdministrative = inscriptionAdministrative;
    }

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionAdministrativeFormationDTO)) {
            return false;
        }

        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = (InscriptionAdministrativeFormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inscriptionAdministrativeFormationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionAdministrativeFormationDTO{" +
            "id=" + getId() +
            ", inscriptionPrincipaleYN='" + getInscriptionPrincipaleYN() + "'" +
            ", inscriptionAnnuleeYN='" + getInscriptionAnnuleeYN() + "'" +
            ", exonereYN='" + getExonereYN() + "'" +
            ", paiementFraisOblYN='" + getPaiementFraisOblYN() + "'" +
            ", paiementFraisIntegergYN='" + getPaiementFraisIntegergYN() + "'" +
            ", certificatDelivreYN='" + getCertificatDelivreYN() + "'" +
            ", dateChoixFormation='" + getDateChoixFormation() + "'" +
            ", dateValidationInscription='" + getDateValidationInscription() + "'" +
            ", inscriptionAdministrative=" + getInscriptionAdministrative() +
            ", formation=" + getFormation() +
            "}";
    }
}
