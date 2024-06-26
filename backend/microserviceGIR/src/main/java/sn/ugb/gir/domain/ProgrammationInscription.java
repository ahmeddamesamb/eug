package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProgrammationInscription.
 */
@Entity
@Table(name = "programmation_inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "programmationinscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgrammationInscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_programmation")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleProgrammation;

    @Column(name = "date_debut_programmation")
    private LocalDate dateDebutProgrammation;

    @Column(name = "date_fin_programmation")
    private LocalDate dateFinProgrammation;

    @Column(name = "ouvert_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean ouvertYN;

    @Column(name = "email_user")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String emailUser;

    @Column(name = "date_forclos_classe")
    private LocalDate dateForclosClasse;

    @Column(name = "forclos_classe_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean forclosClasseYN;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptionAdministratives", "formationInvalides", "programmationInscriptions" }, allowSetters = true)
    private AnneeAcademique anneeAcademique;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "programmationInscriptions" }, allowSetters = true)
    private Campagne campagne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProgrammationInscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleProgrammation() {
        return this.libelleProgrammation;
    }

    public ProgrammationInscription libelleProgrammation(String libelleProgrammation) {
        this.setLibelleProgrammation(libelleProgrammation);
        return this;
    }

    public void setLibelleProgrammation(String libelleProgrammation) {
        this.libelleProgrammation = libelleProgrammation;
    }

    public LocalDate getDateDebutProgrammation() {
        return this.dateDebutProgrammation;
    }

    public ProgrammationInscription dateDebutProgrammation(LocalDate dateDebutProgrammation) {
        this.setDateDebutProgrammation(dateDebutProgrammation);
        return this;
    }

    public void setDateDebutProgrammation(LocalDate dateDebutProgrammation) {
        this.dateDebutProgrammation = dateDebutProgrammation;
    }

    public LocalDate getDateFinProgrammation() {
        return this.dateFinProgrammation;
    }

    public ProgrammationInscription dateFinProgrammation(LocalDate dateFinProgrammation) {
        this.setDateFinProgrammation(dateFinProgrammation);
        return this;
    }

    public void setDateFinProgrammation(LocalDate dateFinProgrammation) {
        this.dateFinProgrammation = dateFinProgrammation;
    }

    public Boolean getOuvertYN() {
        return this.ouvertYN;
    }

    public ProgrammationInscription ouvertYN(Boolean ouvertYN) {
        this.setOuvertYN(ouvertYN);
        return this;
    }

    public void setOuvertYN(Boolean ouvertYN) {
        this.ouvertYN = ouvertYN;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public ProgrammationInscription emailUser(String emailUser) {
        this.setEmailUser(emailUser);
        return this;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public LocalDate getDateForclosClasse() {
        return this.dateForclosClasse;
    }

    public ProgrammationInscription dateForclosClasse(LocalDate dateForclosClasse) {
        this.setDateForclosClasse(dateForclosClasse);
        return this;
    }

    public void setDateForclosClasse(LocalDate dateForclosClasse) {
        this.dateForclosClasse = dateForclosClasse;
    }

    public Boolean getForclosClasseYN() {
        return this.forclosClasseYN;
    }

    public ProgrammationInscription forclosClasseYN(Boolean forclosClasseYN) {
        this.setForclosClasseYN(forclosClasseYN);
        return this;
    }

    public void setForclosClasseYN(Boolean forclosClasseYN) {
        this.forclosClasseYN = forclosClasseYN;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public ProgrammationInscription actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public AnneeAcademique getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public ProgrammationInscription anneeAcademique(AnneeAcademique anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public ProgrammationInscription formation(Formation formation) {
        this.setFormation(formation);
        return this;
    }

    public Campagne getCampagne() {
        return this.campagne;
    }

    public void setCampagne(Campagne campagne) {
        this.campagne = campagne;
    }

    public ProgrammationInscription campagne(Campagne campagne) {
        this.setCampagne(campagne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgrammationInscription)) {
            return false;
        }
        return getId() != null && getId().equals(((ProgrammationInscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgrammationInscription{" +
            "id=" + getId() +
            ", libelleProgrammation='" + getLibelleProgrammation() + "'" +
            ", dateDebutProgrammation='" + getDateDebutProgrammation() + "'" +
            ", dateFinProgrammation='" + getDateFinProgrammation() + "'" +
            ", ouvertYN='" + getOuvertYN() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            ", dateForclosClasse='" + getDateForclosClasse() + "'" +
            ", forclosClasseYN='" + getForclosClasseYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
