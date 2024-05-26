package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InscriptionAdministrativeFormation.
 */
@Entity
@Table(name = "inscription_administrative_formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionAdministrativeFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "inscription_principale_yn")
    private Integer inscriptionPrincipaleYN;

    @Column(name = "inscription_annulee_yn")
    private Integer inscriptionAnnuleeYN;

    @Column(name = "paiement_frais_obl_yn")
    private Integer paiementFraisOblYN;

    @Column(name = "paiement_frais_integerg_yn")
    private Integer paiementFraisIntegergYN;

    @Column(name = "certificat_delivre_yn")
    private Integer certificatDelivreYN;

    @Column(name = "date_choix_formation")
    private Instant dateChoixFormation;

    @Column(name = "date_validation_inscription")
    private Instant dateValidationInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "typeAdmission", "anneeAcademique", "etudiant", "processusDinscriptionAdministrative" },
        allowSetters = true
    )
    private InscriptionAdministrative inscriptionAdministrative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "niveau", "specialite", "formationAutorisees", "formationPrivee" }, allowSetters = true)
    private Formation formation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InscriptionAdministrativeFormation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInscriptionPrincipaleYN() {
        return this.inscriptionPrincipaleYN;
    }

    public InscriptionAdministrativeFormation inscriptionPrincipaleYN(Integer inscriptionPrincipaleYN) {
        this.setInscriptionPrincipaleYN(inscriptionPrincipaleYN);
        return this;
    }

    public void setInscriptionPrincipaleYN(Integer inscriptionPrincipaleYN) {
        this.inscriptionPrincipaleYN = inscriptionPrincipaleYN;
    }

    public Integer getInscriptionAnnuleeYN() {
        return this.inscriptionAnnuleeYN;
    }

    public InscriptionAdministrativeFormation inscriptionAnnuleeYN(Integer inscriptionAnnuleeYN) {
        this.setInscriptionAnnuleeYN(inscriptionAnnuleeYN);
        return this;
    }

    public void setInscriptionAnnuleeYN(Integer inscriptionAnnuleeYN) {
        this.inscriptionAnnuleeYN = inscriptionAnnuleeYN;
    }

    public Integer getPaiementFraisOblYN() {
        return this.paiementFraisOblYN;
    }

    public InscriptionAdministrativeFormation paiementFraisOblYN(Integer paiementFraisOblYN) {
        this.setPaiementFraisOblYN(paiementFraisOblYN);
        return this;
    }

    public void setPaiementFraisOblYN(Integer paiementFraisOblYN) {
        this.paiementFraisOblYN = paiementFraisOblYN;
    }

    public Integer getPaiementFraisIntegergYN() {
        return this.paiementFraisIntegergYN;
    }

    public InscriptionAdministrativeFormation paiementFraisIntegergYN(Integer paiementFraisIntegergYN) {
        this.setPaiementFraisIntegergYN(paiementFraisIntegergYN);
        return this;
    }

    public void setPaiementFraisIntegergYN(Integer paiementFraisIntegergYN) {
        this.paiementFraisIntegergYN = paiementFraisIntegergYN;
    }

    public Integer getCertificatDelivreYN() {
        return this.certificatDelivreYN;
    }

    public InscriptionAdministrativeFormation certificatDelivreYN(Integer certificatDelivreYN) {
        this.setCertificatDelivreYN(certificatDelivreYN);
        return this;
    }

    public void setCertificatDelivreYN(Integer certificatDelivreYN) {
        this.certificatDelivreYN = certificatDelivreYN;
    }

    public Instant getDateChoixFormation() {
        return this.dateChoixFormation;
    }

    public InscriptionAdministrativeFormation dateChoixFormation(Instant dateChoixFormation) {
        this.setDateChoixFormation(dateChoixFormation);
        return this;
    }

    public void setDateChoixFormation(Instant dateChoixFormation) {
        this.dateChoixFormation = dateChoixFormation;
    }

    public Instant getDateValidationInscription() {
        return this.dateValidationInscription;
    }

    public InscriptionAdministrativeFormation dateValidationInscription(Instant dateValidationInscription) {
        this.setDateValidationInscription(dateValidationInscription);
        return this;
    }

    public void setDateValidationInscription(Instant dateValidationInscription) {
        this.dateValidationInscription = dateValidationInscription;
    }

    public InscriptionAdministrative getInscriptionAdministrative() {
        return this.inscriptionAdministrative;
    }

    public void setInscriptionAdministrative(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministrative = inscriptionAdministrative;
    }

    public InscriptionAdministrativeFormation inscriptionAdministrative(InscriptionAdministrative inscriptionAdministrative) {
        this.setInscriptionAdministrative(inscriptionAdministrative);
        return this;
    }

    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public InscriptionAdministrativeFormation formation(Formation formation) {
        this.setFormation(formation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionAdministrativeFormation)) {
            return false;
        }
        return getId() != null && getId().equals(((InscriptionAdministrativeFormation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionAdministrativeFormation{" +
            "id=" + getId() +
            ", inscriptionPrincipaleYN=" + getInscriptionPrincipaleYN() +
            ", inscriptionAnnuleeYN=" + getInscriptionAnnuleeYN() +
            ", paiementFraisOblYN=" + getPaiementFraisOblYN() +
            ", paiementFraisIntegergYN=" + getPaiementFraisIntegergYN() +
            ", certificatDelivreYN=" + getCertificatDelivreYN() +
            ", dateChoixFormation='" + getDateChoixFormation() + "'" +
            ", dateValidationInscription='" + getDateValidationInscription() + "'" +
            "}";
    }
}
