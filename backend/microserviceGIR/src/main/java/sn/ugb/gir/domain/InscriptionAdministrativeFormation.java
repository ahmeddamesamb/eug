package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InscriptionAdministrativeFormation.
 */
@Entity
@Table(name = "inscription_administrative_formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "inscriptionadministrativeformation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionAdministrativeFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "inscription_principale_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean inscriptionPrincipaleYN;

    @Column(name = "inscription_annulee_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean inscriptionAnnuleeYN;

    @NotNull
    @Column(name = "exonere_yn", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean exonereYN;

    @Column(name = "paiement_frais_obl_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean paiementFraisOblYN;

    @Column(name = "paiement_frais_integerg_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean paiementFraisIntegergYN;

    @Column(name = "certificat_delivre_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean certificatDelivreYN;

    @Column(name = "date_choix_formation")
    private Instant dateChoixFormation;

    @Column(name = "date_validation_inscription")
    private Instant dateValidationInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "typeAdmission", "anneeAcademique", "etudiant", "processusDinscriptionAdministrative", "inscriptionAdministrativeFormations",
        },
        allowSetters = true
    )
    private InscriptionAdministrative inscriptionAdministrative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "typeFormation",
            "niveau",
            "specialite",
            "departement",
            "formationPrivee",
            "formationInvalides",
            "inscriptionAdministrativeFormations",
            "programmationInscriptions",
            "formationAutorisees",
        },
        allowSetters = true
    )
    private Formation formation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inscriptionAdministrativeFormation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "doctorat", "inscriptionAdministrativeFormation" }, allowSetters = true)
    private Set<InscriptionDoctorat> inscriptionDoctorats = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inscriptionAdministrativeFormation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "frais", "operateur", "inscriptionAdministrativeFormation" }, allowSetters = true)
    private Set<PaiementFrais> paiementFrais = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inscriptionAdministrativeFormation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "inscriptionAdministrativeFormation", "operateur" }, allowSetters = true)
    private Set<PaiementFormationPrivee> paiementFormationPrivees = new HashSet<>();

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

    public Boolean getInscriptionPrincipaleYN() {
        return this.inscriptionPrincipaleYN;
    }

    public InscriptionAdministrativeFormation inscriptionPrincipaleYN(Boolean inscriptionPrincipaleYN) {
        this.setInscriptionPrincipaleYN(inscriptionPrincipaleYN);
        return this;
    }

    public void setInscriptionPrincipaleYN(Boolean inscriptionPrincipaleYN) {
        this.inscriptionPrincipaleYN = inscriptionPrincipaleYN;
    }

    public Boolean getInscriptionAnnuleeYN() {
        return this.inscriptionAnnuleeYN;
    }

    public InscriptionAdministrativeFormation inscriptionAnnuleeYN(Boolean inscriptionAnnuleeYN) {
        this.setInscriptionAnnuleeYN(inscriptionAnnuleeYN);
        return this;
    }

    public void setInscriptionAnnuleeYN(Boolean inscriptionAnnuleeYN) {
        this.inscriptionAnnuleeYN = inscriptionAnnuleeYN;
    }

    public Boolean getExonereYN() {
        return this.exonereYN;
    }

    public InscriptionAdministrativeFormation exonereYN(Boolean exonereYN) {
        this.setExonereYN(exonereYN);
        return this;
    }

    public void setExonereYN(Boolean exonereYN) {
        this.exonereYN = exonereYN;
    }

    public Boolean getPaiementFraisOblYN() {
        return this.paiementFraisOblYN;
    }

    public InscriptionAdministrativeFormation paiementFraisOblYN(Boolean paiementFraisOblYN) {
        this.setPaiementFraisOblYN(paiementFraisOblYN);
        return this;
    }

    public void setPaiementFraisOblYN(Boolean paiementFraisOblYN) {
        this.paiementFraisOblYN = paiementFraisOblYN;
    }

    public Boolean getPaiementFraisIntegergYN() {
        return this.paiementFraisIntegergYN;
    }

    public InscriptionAdministrativeFormation paiementFraisIntegergYN(Boolean paiementFraisIntegergYN) {
        this.setPaiementFraisIntegergYN(paiementFraisIntegergYN);
        return this;
    }

    public void setPaiementFraisIntegergYN(Boolean paiementFraisIntegergYN) {
        this.paiementFraisIntegergYN = paiementFraisIntegergYN;
    }

    public Boolean getCertificatDelivreYN() {
        return this.certificatDelivreYN;
    }

    public InscriptionAdministrativeFormation certificatDelivreYN(Boolean certificatDelivreYN) {
        this.setCertificatDelivreYN(certificatDelivreYN);
        return this;
    }

    public void setCertificatDelivreYN(Boolean certificatDelivreYN) {
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

    public Set<InscriptionDoctorat> getInscriptionDoctorats() {
        return this.inscriptionDoctorats;
    }

    public void setInscriptionDoctorats(Set<InscriptionDoctorat> inscriptionDoctorats) {
        if (this.inscriptionDoctorats != null) {
            this.inscriptionDoctorats.forEach(i -> i.setInscriptionAdministrativeFormation(null));
        }
        if (inscriptionDoctorats != null) {
            inscriptionDoctorats.forEach(i -> i.setInscriptionAdministrativeFormation(this));
        }
        this.inscriptionDoctorats = inscriptionDoctorats;
    }

    public InscriptionAdministrativeFormation inscriptionDoctorats(Set<InscriptionDoctorat> inscriptionDoctorats) {
        this.setInscriptionDoctorats(inscriptionDoctorats);
        return this;
    }

    public InscriptionAdministrativeFormation addInscriptionDoctorats(InscriptionDoctorat inscriptionDoctorat) {
        this.inscriptionDoctorats.add(inscriptionDoctorat);
        inscriptionDoctorat.setInscriptionAdministrativeFormation(this);
        return this;
    }

    public InscriptionAdministrativeFormation removeInscriptionDoctorats(InscriptionDoctorat inscriptionDoctorat) {
        this.inscriptionDoctorats.remove(inscriptionDoctorat);
        inscriptionDoctorat.setInscriptionAdministrativeFormation(null);
        return this;
    }

    public Set<PaiementFrais> getPaiementFrais() {
        return this.paiementFrais;
    }

    public void setPaiementFrais(Set<PaiementFrais> paiementFrais) {
        if (this.paiementFrais != null) {
            this.paiementFrais.forEach(i -> i.setInscriptionAdministrativeFormation(null));
        }
        if (paiementFrais != null) {
            paiementFrais.forEach(i -> i.setInscriptionAdministrativeFormation(this));
        }
        this.paiementFrais = paiementFrais;
    }

    public InscriptionAdministrativeFormation paiementFrais(Set<PaiementFrais> paiementFrais) {
        this.setPaiementFrais(paiementFrais);
        return this;
    }

    public InscriptionAdministrativeFormation addPaiementFrais(PaiementFrais paiementFrais) {
        this.paiementFrais.add(paiementFrais);
        paiementFrais.setInscriptionAdministrativeFormation(this);
        return this;
    }

    public InscriptionAdministrativeFormation removePaiementFrais(PaiementFrais paiementFrais) {
        this.paiementFrais.remove(paiementFrais);
        paiementFrais.setInscriptionAdministrativeFormation(null);
        return this;
    }

    public Set<PaiementFormationPrivee> getPaiementFormationPrivees() {
        return this.paiementFormationPrivees;
    }

    public void setPaiementFormationPrivees(Set<PaiementFormationPrivee> paiementFormationPrivees) {
        if (this.paiementFormationPrivees != null) {
            this.paiementFormationPrivees.forEach(i -> i.setInscriptionAdministrativeFormation(null));
        }
        if (paiementFormationPrivees != null) {
            paiementFormationPrivees.forEach(i -> i.setInscriptionAdministrativeFormation(this));
        }
        this.paiementFormationPrivees = paiementFormationPrivees;
    }

    public InscriptionAdministrativeFormation paiementFormationPrivees(Set<PaiementFormationPrivee> paiementFormationPrivees) {
        this.setPaiementFormationPrivees(paiementFormationPrivees);
        return this;
    }

    public InscriptionAdministrativeFormation addPaiementFormationPrivees(PaiementFormationPrivee paiementFormationPrivee) {
        this.paiementFormationPrivees.add(paiementFormationPrivee);
        paiementFormationPrivee.setInscriptionAdministrativeFormation(this);
        return this;
    }

    public InscriptionAdministrativeFormation removePaiementFormationPrivees(PaiementFormationPrivee paiementFormationPrivee) {
        this.paiementFormationPrivees.remove(paiementFormationPrivee);
        paiementFormationPrivee.setInscriptionAdministrativeFormation(null);
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
            ", inscriptionPrincipaleYN='" + getInscriptionPrincipaleYN() + "'" +
            ", inscriptionAnnuleeYN='" + getInscriptionAnnuleeYN() + "'" +
            ", exonereYN='" + getExonereYN() + "'" +
            ", paiementFraisOblYN='" + getPaiementFraisOblYN() + "'" +
            ", paiementFraisIntegergYN='" + getPaiementFraisIntegergYN() + "'" +
            ", certificatDelivreYN='" + getCertificatDelivreYN() + "'" +
            ", dateChoixFormation='" + getDateChoixFormation() + "'" +
            ", dateValidationInscription='" + getDateValidationInscription() + "'" +
            "}";
    }
}
