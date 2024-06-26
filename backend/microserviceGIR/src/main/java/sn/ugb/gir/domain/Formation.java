package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "classe_diplomante_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean classeDiplomanteYN;

    @Column(name = "libelle_diplome")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleDiplome;

    @Column(name = "code_formation")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codeFormation;

    @Column(name = "nbre_credits_min")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbreCreditsMin;

    @Column(name = "est_parcours_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean estParcoursYN;

    @Column(name = "lmd_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean lmdYN;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "formations" }, allowSetters = true)
    private TypeFormation typeFormation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "typeCycle", "formations" }, allowSetters = true)
    private Niveau niveau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mention", "formations" }, allowSetters = true)
    private Specialite specialite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ufr" }, allowSetters = true)
    private Departement departement;

    @JsonIgnoreProperties(value = { "formation" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "formation")
    @org.springframework.data.annotation.Transient
    private FormationPrivee formationPrivee;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "formation", "anneeAcademique" }, allowSetters = true)
    private Set<FormationInvalide> formationInvalides = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "inscriptionAdministrative", "formation", "inscriptionDoctorats", "paiementFrais", "paiementFormationPrivees" },
        allowSetters = true
    )
    private Set<InscriptionAdministrativeFormation> inscriptionAdministrativeFormations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "anneeAcademique", "formation", "campagne" }, allowSetters = true)
    private Set<ProgrammationInscription> programmationInscriptions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "formations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "formations" }, allowSetters = true)
    private Set<FormationAutorisee> formationAutorisees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Formation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getClasseDiplomanteYN() {
        return this.classeDiplomanteYN;
    }

    public Formation classeDiplomanteYN(Boolean classeDiplomanteYN) {
        this.setClasseDiplomanteYN(classeDiplomanteYN);
        return this;
    }

    public void setClasseDiplomanteYN(Boolean classeDiplomanteYN) {
        this.classeDiplomanteYN = classeDiplomanteYN;
    }

    public String getLibelleDiplome() {
        return this.libelleDiplome;
    }

    public Formation libelleDiplome(String libelleDiplome) {
        this.setLibelleDiplome(libelleDiplome);
        return this;
    }

    public void setLibelleDiplome(String libelleDiplome) {
        this.libelleDiplome = libelleDiplome;
    }

    public String getCodeFormation() {
        return this.codeFormation;
    }

    public Formation codeFormation(String codeFormation) {
        this.setCodeFormation(codeFormation);
        return this;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
    }

    public Integer getNbreCreditsMin() {
        return this.nbreCreditsMin;
    }

    public Formation nbreCreditsMin(Integer nbreCreditsMin) {
        this.setNbreCreditsMin(nbreCreditsMin);
        return this;
    }

    public void setNbreCreditsMin(Integer nbreCreditsMin) {
        this.nbreCreditsMin = nbreCreditsMin;
    }

    public Boolean getEstParcoursYN() {
        return this.estParcoursYN;
    }

    public Formation estParcoursYN(Boolean estParcoursYN) {
        this.setEstParcoursYN(estParcoursYN);
        return this;
    }

    public void setEstParcoursYN(Boolean estParcoursYN) {
        this.estParcoursYN = estParcoursYN;
    }

    public Boolean getLmdYN() {
        return this.lmdYN;
    }

    public Formation lmdYN(Boolean lmdYN) {
        this.setLmdYN(lmdYN);
        return this;
    }

    public void setLmdYN(Boolean lmdYN) {
        this.lmdYN = lmdYN;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Formation actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public TypeFormation getTypeFormation() {
        return this.typeFormation;
    }

    public void setTypeFormation(TypeFormation typeFormation) {
        this.typeFormation = typeFormation;
    }

    public Formation typeFormation(TypeFormation typeFormation) {
        this.setTypeFormation(typeFormation);
        return this;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Formation niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public Specialite getSpecialite() {
        return this.specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public Formation specialite(Specialite specialite) {
        this.setSpecialite(specialite);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Formation departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public FormationPrivee getFormationPrivee() {
        return this.formationPrivee;
    }

    public void setFormationPrivee(FormationPrivee formationPrivee) {
        if (this.formationPrivee != null) {
            this.formationPrivee.setFormation(null);
        }
        if (formationPrivee != null) {
            formationPrivee.setFormation(this);
        }
        this.formationPrivee = formationPrivee;
    }

    public Formation formationPrivee(FormationPrivee formationPrivee) {
        this.setFormationPrivee(formationPrivee);
        return this;
    }

    public Set<FormationInvalide> getFormationInvalides() {
        return this.formationInvalides;
    }

    public void setFormationInvalides(Set<FormationInvalide> formationInvalides) {
        if (this.formationInvalides != null) {
            this.formationInvalides.forEach(i -> i.setFormation(null));
        }
        if (formationInvalides != null) {
            formationInvalides.forEach(i -> i.setFormation(this));
        }
        this.formationInvalides = formationInvalides;
    }

    public Formation formationInvalides(Set<FormationInvalide> formationInvalides) {
        this.setFormationInvalides(formationInvalides);
        return this;
    }

    public Formation addFormationInvalides(FormationInvalide formationInvalide) {
        this.formationInvalides.add(formationInvalide);
        formationInvalide.setFormation(this);
        return this;
    }

    public Formation removeFormationInvalides(FormationInvalide formationInvalide) {
        this.formationInvalides.remove(formationInvalide);
        formationInvalide.setFormation(null);
        return this;
    }

    public Set<InscriptionAdministrativeFormation> getInscriptionAdministrativeFormations() {
        return this.inscriptionAdministrativeFormations;
    }

    public void setInscriptionAdministrativeFormations(Set<InscriptionAdministrativeFormation> inscriptionAdministrativeFormations) {
        if (this.inscriptionAdministrativeFormations != null) {
            this.inscriptionAdministrativeFormations.forEach(i -> i.setFormation(null));
        }
        if (inscriptionAdministrativeFormations != null) {
            inscriptionAdministrativeFormations.forEach(i -> i.setFormation(this));
        }
        this.inscriptionAdministrativeFormations = inscriptionAdministrativeFormations;
    }

    public Formation inscriptionAdministrativeFormations(Set<InscriptionAdministrativeFormation> inscriptionAdministrativeFormations) {
        this.setInscriptionAdministrativeFormations(inscriptionAdministrativeFormations);
        return this;
    }

    public Formation addInscriptionAdministrativeFormations(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormations.add(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormation.setFormation(this);
        return this;
    }

    public Formation removeInscriptionAdministrativeFormations(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormations.remove(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormation.setFormation(null);
        return this;
    }

    public Set<ProgrammationInscription> getProgrammationInscriptions() {
        return this.programmationInscriptions;
    }

    public void setProgrammationInscriptions(Set<ProgrammationInscription> programmationInscriptions) {
        if (this.programmationInscriptions != null) {
            this.programmationInscriptions.forEach(i -> i.setFormation(null));
        }
        if (programmationInscriptions != null) {
            programmationInscriptions.forEach(i -> i.setFormation(this));
        }
        this.programmationInscriptions = programmationInscriptions;
    }

    public Formation programmationInscriptions(Set<ProgrammationInscription> programmationInscriptions) {
        this.setProgrammationInscriptions(programmationInscriptions);
        return this;
    }

    public Formation addProgrammationInscriptions(ProgrammationInscription programmationInscription) {
        this.programmationInscriptions.add(programmationInscription);
        programmationInscription.setFormation(this);
        return this;
    }

    public Formation removeProgrammationInscriptions(ProgrammationInscription programmationInscription) {
        this.programmationInscriptions.remove(programmationInscription);
        programmationInscription.setFormation(null);
        return this;
    }

    public Set<FormationAutorisee> getFormationAutorisees() {
        return this.formationAutorisees;
    }

    public void setFormationAutorisees(Set<FormationAutorisee> formationAutorisees) {
        if (this.formationAutorisees != null) {
            this.formationAutorisees.forEach(i -> i.removeFormation(this));
        }
        if (formationAutorisees != null) {
            formationAutorisees.forEach(i -> i.addFormation(this));
        }
        this.formationAutorisees = formationAutorisees;
    }

    public Formation formationAutorisees(Set<FormationAutorisee> formationAutorisees) {
        this.setFormationAutorisees(formationAutorisees);
        return this;
    }

    public Formation addFormationAutorisee(FormationAutorisee formationAutorisee) {
        this.formationAutorisees.add(formationAutorisee);
        formationAutorisee.getFormations().add(this);
        return this;
    }

    public Formation removeFormationAutorisee(FormationAutorisee formationAutorisee) {
        this.formationAutorisees.remove(formationAutorisee);
        formationAutorisee.getFormations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formation)) {
            return false;
        }
        return getId() != null && getId().equals(((Formation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formation{" +
            "id=" + getId() +
            ", classeDiplomanteYN='" + getClasseDiplomanteYN() + "'" +
            ", libelleDiplome='" + getLibelleDiplome() + "'" +
            ", codeFormation='" + getCodeFormation() + "'" +
            ", nbreCreditsMin=" + getNbreCreditsMin() +
            ", estParcoursYN='" + getEstParcoursYN() + "'" +
            ", lmdYN='" + getLmdYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
