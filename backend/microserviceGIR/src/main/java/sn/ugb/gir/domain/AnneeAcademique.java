package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AnneeAcademique.
 */
@Entity
@Table(name = "annee_academique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "anneeacademique")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnneeAcademique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_annee_academique", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleAnneeAcademique;

    @NotNull
    @Min(value = 1990)
    @Max(value = 2060)
    @Column(name = "annee_ac", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer anneeAc;

    @NotNull
    @Size(max = 1)
    @Column(name = "separateur", length = 1, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String separateur;

    @Column(name = "annee_courante_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean anneeCouranteYN;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "anneeAcademique")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "typeAdmission", "anneeAcademique", "etudiant", "processusDinscriptionAdministrative", "inscriptionAdministrativeFormations",
        },
        allowSetters = true
    )
    private Set<InscriptionAdministrative> inscriptionAdministratives = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "anneeAcademique")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "formation", "anneeAcademique" }, allowSetters = true)
    private Set<FormationInvalide> formationInvalides = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "anneeAcademique")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "anneeAcademique", "formation", "campagne" }, allowSetters = true)
    private Set<ProgrammationInscription> programmationInscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnneeAcademique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleAnneeAcademique() {
        return this.libelleAnneeAcademique;
    }

    public AnneeAcademique libelleAnneeAcademique(String libelleAnneeAcademique) {
        this.setLibelleAnneeAcademique(libelleAnneeAcademique);
        return this;
    }

    public void setLibelleAnneeAcademique(String libelleAnneeAcademique) {
        this.libelleAnneeAcademique = libelleAnneeAcademique;
    }

    public Integer getAnneeAc() {
        return this.anneeAc;
    }

    public AnneeAcademique anneeAc(Integer anneeAc) {
        this.setAnneeAc(anneeAc);
        return this;
    }

    public void setAnneeAc(Integer anneeAc) {
        this.anneeAc = anneeAc;
    }

    public String getSeparateur() {
        return this.separateur;
    }

    public AnneeAcademique separateur(String separateur) {
        this.setSeparateur(separateur);
        return this;
    }

    public void setSeparateur(String separateur) {
        this.separateur = separateur;
    }

    public Boolean getAnneeCouranteYN() {
        return this.anneeCouranteYN;
    }

    public AnneeAcademique anneeCouranteYN(Boolean anneeCouranteYN) {
        this.setAnneeCouranteYN(anneeCouranteYN);
        return this;
    }

    public void setAnneeCouranteYN(Boolean anneeCouranteYN) {
        this.anneeCouranteYN = anneeCouranteYN;
    }

    public Set<InscriptionAdministrative> getInscriptionAdministratives() {
        return this.inscriptionAdministratives;
    }

    public void setInscriptionAdministratives(Set<InscriptionAdministrative> inscriptionAdministratives) {
        if (this.inscriptionAdministratives != null) {
            this.inscriptionAdministratives.forEach(i -> i.setAnneeAcademique(null));
        }
        if (inscriptionAdministratives != null) {
            inscriptionAdministratives.forEach(i -> i.setAnneeAcademique(this));
        }
        this.inscriptionAdministratives = inscriptionAdministratives;
    }

    public AnneeAcademique inscriptionAdministratives(Set<InscriptionAdministrative> inscriptionAdministratives) {
        this.setInscriptionAdministratives(inscriptionAdministratives);
        return this;
    }

    public AnneeAcademique addInscriptionAdministratives(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministratives.add(inscriptionAdministrative);
        inscriptionAdministrative.setAnneeAcademique(this);
        return this;
    }

    public AnneeAcademique removeInscriptionAdministratives(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministratives.remove(inscriptionAdministrative);
        inscriptionAdministrative.setAnneeAcademique(null);
        return this;
    }

    public Set<FormationInvalide> getFormationInvalides() {
        return this.formationInvalides;
    }

    public void setFormationInvalides(Set<FormationInvalide> formationInvalides) {
        if (this.formationInvalides != null) {
            this.formationInvalides.forEach(i -> i.setAnneeAcademique(null));
        }
        if (formationInvalides != null) {
            formationInvalides.forEach(i -> i.setAnneeAcademique(this));
        }
        this.formationInvalides = formationInvalides;
    }

    public AnneeAcademique formationInvalides(Set<FormationInvalide> formationInvalides) {
        this.setFormationInvalides(formationInvalides);
        return this;
    }

    public AnneeAcademique addFormationInvalides(FormationInvalide formationInvalide) {
        this.formationInvalides.add(formationInvalide);
        formationInvalide.setAnneeAcademique(this);
        return this;
    }

    public AnneeAcademique removeFormationInvalides(FormationInvalide formationInvalide) {
        this.formationInvalides.remove(formationInvalide);
        formationInvalide.setAnneeAcademique(null);
        return this;
    }

    public Set<ProgrammationInscription> getProgrammationInscriptions() {
        return this.programmationInscriptions;
    }

    public void setProgrammationInscriptions(Set<ProgrammationInscription> programmationInscriptions) {
        if (this.programmationInscriptions != null) {
            this.programmationInscriptions.forEach(i -> i.setAnneeAcademique(null));
        }
        if (programmationInscriptions != null) {
            programmationInscriptions.forEach(i -> i.setAnneeAcademique(this));
        }
        this.programmationInscriptions = programmationInscriptions;
    }

    public AnneeAcademique programmationInscriptions(Set<ProgrammationInscription> programmationInscriptions) {
        this.setProgrammationInscriptions(programmationInscriptions);
        return this;
    }

    public AnneeAcademique addProgrammationInscriptions(ProgrammationInscription programmationInscription) {
        this.programmationInscriptions.add(programmationInscription);
        programmationInscription.setAnneeAcademique(this);
        return this;
    }

    public AnneeAcademique removeProgrammationInscriptions(ProgrammationInscription programmationInscription) {
        this.programmationInscriptions.remove(programmationInscription);
        programmationInscription.setAnneeAcademique(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnneeAcademique)) {
            return false;
        }
        return getId() != null && getId().equals(((AnneeAcademique) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnneeAcademique{" +
            "id=" + getId() +
            ", libelleAnneeAcademique='" + getLibelleAnneeAcademique() + "'" +
            ", anneeAc=" + getAnneeAc() +
            ", separateur='" + getSeparateur() + "'" +
            ", anneeCouranteYN='" + getAnneeCouranteYN() + "'" +
            "}";
    }
}
