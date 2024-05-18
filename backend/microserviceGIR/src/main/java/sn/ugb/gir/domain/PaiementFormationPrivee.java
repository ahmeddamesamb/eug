package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaiementFormationPrivee.
 */
@Entity
@Table(name = "paiement_formation_privee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementFormationPrivee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_paiement")
    private Instant datePaiement;

    @Column(name = "mois_paiement")
    private String moisPaiement;

    @Column(name = "annee_paiement")
    private String anneePaiement;

    @Column(name = "payer_mensualite_yn")
    private Integer payerMensualiteYN;

    @Column(name = "email_user")
    private String emailUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptionAdministrative", "formation" }, allowSetters = true)
    private InscriptionAdministrativeFormation inscriptionAdministrativeFormation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operateur operateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaiementFormationPrivee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatePaiement() {
        return this.datePaiement;
    }

    public PaiementFormationPrivee datePaiement(Instant datePaiement) {
        this.setDatePaiement(datePaiement);
        return this;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getMoisPaiement() {
        return this.moisPaiement;
    }

    public PaiementFormationPrivee moisPaiement(String moisPaiement) {
        this.setMoisPaiement(moisPaiement);
        return this;
    }

    public void setMoisPaiement(String moisPaiement) {
        this.moisPaiement = moisPaiement;
    }

    public String getAnneePaiement() {
        return this.anneePaiement;
    }

    public PaiementFormationPrivee anneePaiement(String anneePaiement) {
        this.setAnneePaiement(anneePaiement);
        return this;
    }

    public void setAnneePaiement(String anneePaiement) {
        this.anneePaiement = anneePaiement;
    }

    public Integer getPayerMensualiteYN() {
        return this.payerMensualiteYN;
    }

    public PaiementFormationPrivee payerMensualiteYN(Integer payerMensualiteYN) {
        this.setPayerMensualiteYN(payerMensualiteYN);
        return this;
    }

    public void setPayerMensualiteYN(Integer payerMensualiteYN) {
        this.payerMensualiteYN = payerMensualiteYN;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public PaiementFormationPrivee emailUser(String emailUser) {
        this.setEmailUser(emailUser);
        return this;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public InscriptionAdministrativeFormation getInscriptionAdministrativeFormation() {
        return this.inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    public PaiementFormationPrivee inscriptionAdministrativeFormation(
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation
    ) {
        this.setInscriptionAdministrativeFormation(inscriptionAdministrativeFormation);
        return this;
    }

    public Operateur getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public PaiementFormationPrivee operateur(Operateur operateur) {
        this.setOperateur(operateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementFormationPrivee)) {
            return false;
        }
        return getId() != null && getId().equals(((PaiementFormationPrivee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementFormationPrivee{" +
            "id=" + getId() +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", moisPaiement='" + getMoisPaiement() + "'" +
            ", anneePaiement='" + getAnneePaiement() + "'" +
            ", payerMensualiteYN=" + getPayerMensualiteYN() +
            ", emailUser='" + getEmailUser() + "'" +
            "}";
    }
}
