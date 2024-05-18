package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.PaiementFormationPrivee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementFormationPriveeDTO implements Serializable {

    private Long id;

    private Instant datePaiement;

    private String moisPaiement;

    private String anneePaiement;

    private Integer payerMensualiteYN;

    private String emailUser;

    private InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation;

    private OperateurDTO operateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getMoisPaiement() {
        return moisPaiement;
    }

    public void setMoisPaiement(String moisPaiement) {
        this.moisPaiement = moisPaiement;
    }

    public String getAnneePaiement() {
        return anneePaiement;
    }

    public void setAnneePaiement(String anneePaiement) {
        this.anneePaiement = anneePaiement;
    }

    public Integer getPayerMensualiteYN() {
        return payerMensualiteYN;
    }

    public void setPayerMensualiteYN(Integer payerMensualiteYN) {
        this.payerMensualiteYN = payerMensualiteYN;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public InscriptionAdministrativeFormationDTO getInscriptionAdministrativeFormation() {
        return inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    public OperateurDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(OperateurDTO operateur) {
        this.operateur = operateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementFormationPriveeDTO)) {
            return false;
        }

        PaiementFormationPriveeDTO paiementFormationPriveeDTO = (PaiementFormationPriveeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementFormationPriveeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementFormationPriveeDTO{" +
            "id=" + getId() +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", moisPaiement='" + getMoisPaiement() + "'" +
            ", anneePaiement='" + getAnneePaiement() + "'" +
            ", payerMensualiteYN=" + getPayerMensualiteYN() +
            ", emailUser='" + getEmailUser() + "'" +
            ", inscriptionAdministrativeFormation=" + getInscriptionAdministrativeFormation() +
            ", operateur=" + getOperateur() +
            "}";
    }
}
