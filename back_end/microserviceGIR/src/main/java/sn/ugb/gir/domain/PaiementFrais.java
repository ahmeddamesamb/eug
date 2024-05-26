package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaiementFrais.
 */
@Entity
@Table(name = "paiement_frais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementFrais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;

    @Column(name = "obligatoire_yn")
    private Integer obligatoireYN;

    @Column(name = "echeance_payee_yn")
    private Integer echeancePayeeYN;

    @Column(name = "email_user")
    private String emailUser;

    @Column(name = "date_forclos")
    private LocalDate dateForclos;

    @Column(name = "forclos_yn")
    private Integer forclosYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "typeFrais" }, allowSetters = true)
    private Frais frais;

    @ManyToOne(fetch = FetchType.LAZY)
    private Operateur operateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptionAdministrative", "formation" }, allowSetters = true)
    private InscriptionAdministrativeFormation inscriptionAdministrativeFormation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaiementFrais id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatePaiement() {
        return this.datePaiement;
    }

    public PaiementFrais datePaiement(LocalDate datePaiement) {
        this.setDatePaiement(datePaiement);
        return this;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Integer getObligatoireYN() {
        return this.obligatoireYN;
    }

    public PaiementFrais obligatoireYN(Integer obligatoireYN) {
        this.setObligatoireYN(obligatoireYN);
        return this;
    }

    public void setObligatoireYN(Integer obligatoireYN) {
        this.obligatoireYN = obligatoireYN;
    }

    public Integer getEcheancePayeeYN() {
        return this.echeancePayeeYN;
    }

    public PaiementFrais echeancePayeeYN(Integer echeancePayeeYN) {
        this.setEcheancePayeeYN(echeancePayeeYN);
        return this;
    }

    public void setEcheancePayeeYN(Integer echeancePayeeYN) {
        this.echeancePayeeYN = echeancePayeeYN;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public PaiementFrais emailUser(String emailUser) {
        this.setEmailUser(emailUser);
        return this;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public LocalDate getDateForclos() {
        return this.dateForclos;
    }

    public PaiementFrais dateForclos(LocalDate dateForclos) {
        this.setDateForclos(dateForclos);
        return this;
    }

    public void setDateForclos(LocalDate dateForclos) {
        this.dateForclos = dateForclos;
    }

    public Integer getForclosYN() {
        return this.forclosYN;
    }

    public PaiementFrais forclosYN(Integer forclosYN) {
        this.setForclosYN(forclosYN);
        return this;
    }

    public void setForclosYN(Integer forclosYN) {
        this.forclosYN = forclosYN;
    }

    public Frais getFrais() {
        return this.frais;
    }

    public void setFrais(Frais frais) {
        this.frais = frais;
    }

    public PaiementFrais frais(Frais frais) {
        this.setFrais(frais);
        return this;
    }

    public Operateur getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public PaiementFrais operateur(Operateur operateur) {
        this.setOperateur(operateur);
        return this;
    }

    public InscriptionAdministrativeFormation getInscriptionAdministrativeFormation() {
        return this.inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    public PaiementFrais inscriptionAdministrativeFormation(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.setInscriptionAdministrativeFormation(inscriptionAdministrativeFormation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementFrais)) {
            return false;
        }
        return getId() != null && getId().equals(((PaiementFrais) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementFrais{" +
            "id=" + getId() +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", obligatoireYN=" + getObligatoireYN() +
            ", echeancePayeeYN=" + getEcheancePayeeYN() +
            ", emailUser='" + getEmailUser() + "'" +
            ", dateForclos='" + getDateForclos() + "'" +
            ", forclosYN=" + getForclosYN() +
            "}";
    }
}
