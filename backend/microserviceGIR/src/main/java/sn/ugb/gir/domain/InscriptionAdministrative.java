package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InscriptionAdministrative.
 */
@Entity
@Table(name = "inscription_administrative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "inscriptionadministrative")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionAdministrative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nouveau_inscrit_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean nouveauInscritYN;

    @Column(name = "reprise_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean repriseYN;

    @Column(name = "autorise_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean autoriseYN;

    @Column(name = "ordre_inscription")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer ordreInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptionAdministratives" }, allowSetters = true)
    private TypeAdmission typeAdmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptionAdministratives", "formationInvalides", "programmationInscriptions" }, allowSetters = true)
    private AnneeAcademique anneeAcademique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "region",
            "typeSelection",
            "lycee",
            "informationPersonnelle",
            "baccalaureat",
            "disciplineSportiveEtudiants",
            "inscriptionAdministratives",
        },
        allowSetters = true
    )
    private Etudiant etudiant;

    @JsonIgnoreProperties(value = { "inscriptionAdministrative" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "inscriptionAdministrative")
    @org.springframework.data.annotation.Transient
    private ProcessusInscriptionAdministrative processusDinscriptionAdministrative;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inscriptionAdministrative")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "inscriptionAdministrative", "formation", "inscriptionDoctorats", "paiementFrais", "paiementFormationPrivees" },
        allowSetters = true
    )
    private Set<InscriptionAdministrativeFormation> inscriptionAdministrativeFormations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InscriptionAdministrative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getNouveauInscritYN() {
        return this.nouveauInscritYN;
    }

    public InscriptionAdministrative nouveauInscritYN(Boolean nouveauInscritYN) {
        this.setNouveauInscritYN(nouveauInscritYN);
        return this;
    }

    public void setNouveauInscritYN(Boolean nouveauInscritYN) {
        this.nouveauInscritYN = nouveauInscritYN;
    }

    public Boolean getRepriseYN() {
        return this.repriseYN;
    }

    public InscriptionAdministrative repriseYN(Boolean repriseYN) {
        this.setRepriseYN(repriseYN);
        return this;
    }

    public void setRepriseYN(Boolean repriseYN) {
        this.repriseYN = repriseYN;
    }

    public Boolean getAutoriseYN() {
        return this.autoriseYN;
    }

    public InscriptionAdministrative autoriseYN(Boolean autoriseYN) {
        this.setAutoriseYN(autoriseYN);
        return this;
    }

    public void setAutoriseYN(Boolean autoriseYN) {
        this.autoriseYN = autoriseYN;
    }

    public Integer getOrdreInscription() {
        return this.ordreInscription;
    }

    public InscriptionAdministrative ordreInscription(Integer ordreInscription) {
        this.setOrdreInscription(ordreInscription);
        return this;
    }

    public void setOrdreInscription(Integer ordreInscription) {
        this.ordreInscription = ordreInscription;
    }

    public TypeAdmission getTypeAdmission() {
        return this.typeAdmission;
    }

    public void setTypeAdmission(TypeAdmission typeAdmission) {
        this.typeAdmission = typeAdmission;
    }

    public InscriptionAdministrative typeAdmission(TypeAdmission typeAdmission) {
        this.setTypeAdmission(typeAdmission);
        return this;
    }

    public AnneeAcademique getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public InscriptionAdministrative anneeAcademique(AnneeAcademique anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public InscriptionAdministrative etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public ProcessusInscriptionAdministrative getProcessusDinscriptionAdministrative() {
        return this.processusDinscriptionAdministrative;
    }

    public void setProcessusDinscriptionAdministrative(ProcessusInscriptionAdministrative processusInscriptionAdministrative) {
        if (this.processusDinscriptionAdministrative != null) {
            this.processusDinscriptionAdministrative.setInscriptionAdministrative(null);
        }
        if (processusInscriptionAdministrative != null) {
            processusInscriptionAdministrative.setInscriptionAdministrative(this);
        }
        this.processusDinscriptionAdministrative = processusInscriptionAdministrative;
    }

    public InscriptionAdministrative processusDinscriptionAdministrative(
        ProcessusInscriptionAdministrative processusInscriptionAdministrative
    ) {
        this.setProcessusDinscriptionAdministrative(processusInscriptionAdministrative);
        return this;
    }

    public Set<InscriptionAdministrativeFormation> getInscriptionAdministrativeFormations() {
        return this.inscriptionAdministrativeFormations;
    }

    public void setInscriptionAdministrativeFormations(Set<InscriptionAdministrativeFormation> inscriptionAdministrativeFormations) {
        if (this.inscriptionAdministrativeFormations != null) {
            this.inscriptionAdministrativeFormations.forEach(i -> i.setInscriptionAdministrative(null));
        }
        if (inscriptionAdministrativeFormations != null) {
            inscriptionAdministrativeFormations.forEach(i -> i.setInscriptionAdministrative(this));
        }
        this.inscriptionAdministrativeFormations = inscriptionAdministrativeFormations;
    }

    public InscriptionAdministrative inscriptionAdministrativeFormations(
        Set<InscriptionAdministrativeFormation> inscriptionAdministrativeFormations
    ) {
        this.setInscriptionAdministrativeFormations(inscriptionAdministrativeFormations);
        return this;
    }

    public InscriptionAdministrative addInscriptionAdministrativeFormations(
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation
    ) {
        this.inscriptionAdministrativeFormations.add(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormation.setInscriptionAdministrative(this);
        return this;
    }

    public InscriptionAdministrative removeInscriptionAdministrativeFormations(
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation
    ) {
        this.inscriptionAdministrativeFormations.remove(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormation.setInscriptionAdministrative(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionAdministrative)) {
            return false;
        }
        return getId() != null && getId().equals(((InscriptionAdministrative) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionAdministrative{" +
            "id=" + getId() +
            ", nouveauInscritYN='" + getNouveauInscritYN() + "'" +
            ", repriseYN='" + getRepriseYN() + "'" +
            ", autoriseYN='" + getAutoriseYN() + "'" +
            ", ordreInscription=" + getOrdreInscription() +
            "}";
    }
}
