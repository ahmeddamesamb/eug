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
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgrammationInscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_programmation")
    private String libelleProgrammation;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "ouvert_yn")
    private Integer ouvertYN;

    @Column(name = "email_user")
    private String emailUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private AnneeAcademique anneeAcademique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "niveau", "specialite", "formationAutorisees", "formationPrivee" }, allowSetters = true)
    private Formation formation;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public ProgrammationInscription dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public ProgrammationInscription dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getOuvertYN() {
        return this.ouvertYN;
    }

    public ProgrammationInscription ouvertYN(Integer ouvertYN) {
        this.setOuvertYN(ouvertYN);
        return this;
    }

    public void setOuvertYN(Integer ouvertYN) {
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
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", ouvertYN=" + getOuvertYN() +
            ", emailUser='" + getEmailUser() + "'" +
            "}";
    }
}
