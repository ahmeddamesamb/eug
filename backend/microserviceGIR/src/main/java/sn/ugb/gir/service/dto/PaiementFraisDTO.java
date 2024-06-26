package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.PaiementFrais} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementFraisDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate datePaiement;

    private Boolean obligatoireYN;

    private Boolean echeancePayeeYN;

    private String emailUser;

    private LocalDate dateForclos;

    private Boolean forclosYN;

    private Boolean paimentDelaiYN;

    private FraisDTO frais;

    private OperateurDTO operateur;

    private InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Boolean getObligatoireYN() {
        return obligatoireYN;
    }

    public void setObligatoireYN(Boolean obligatoireYN) {
        this.obligatoireYN = obligatoireYN;
    }

    public Boolean getEcheancePayeeYN() {
        return echeancePayeeYN;
    }

    public void setEcheancePayeeYN(Boolean echeancePayeeYN) {
        this.echeancePayeeYN = echeancePayeeYN;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public LocalDate getDateForclos() {
        return dateForclos;
    }

    public void setDateForclos(LocalDate dateForclos) {
        this.dateForclos = dateForclos;
    }

    public Boolean getForclosYN() {
        return forclosYN;
    }

    public void setForclosYN(Boolean forclosYN) {
        this.forclosYN = forclosYN;
    }

    public Boolean getPaimentDelaiYN() {
        return paimentDelaiYN;
    }

    public void setPaimentDelaiYN(Boolean paimentDelaiYN) {
        this.paimentDelaiYN = paimentDelaiYN;
    }

    public FraisDTO getFrais() {
        return frais;
    }

    public void setFrais(FraisDTO frais) {
        this.frais = frais;
    }

    public OperateurDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(OperateurDTO operateur) {
        this.operateur = operateur;
    }

    public InscriptionAdministrativeFormationDTO getInscriptionAdministrativeFormation() {
        return inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementFraisDTO)) {
            return false;
        }

        PaiementFraisDTO paiementFraisDTO = (PaiementFraisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementFraisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementFraisDTO{" +
            "id=" + getId() +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", obligatoireYN='" + getObligatoireYN() + "'" +
            ", echeancePayeeYN='" + getEcheancePayeeYN() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            ", dateForclos='" + getDateForclos() + "'" +
            ", forclosYN='" + getForclosYN() + "'" +
            ", paimentDelaiYN='" + getPaimentDelaiYN() + "'" +
            ", frais=" + getFrais() +
            ", operateur=" + getOperateur() +
            ", inscriptionAdministrativeFormation=" + getInscriptionAdministrativeFormation() +
            "}";
    }
}
