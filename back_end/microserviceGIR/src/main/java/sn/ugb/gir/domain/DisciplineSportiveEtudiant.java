package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DisciplineSportiveEtudiant.
 */
@Entity
@Table(name = "discipline_sportive_etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisciplineSportiveEtudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull //Ajoutee
    @Column(name = "licence_sportive_yn", nullable = false)
    private Integer licenceSportiveYN;

    @NotNull //Ajoutee
    @Column(name = "competition_ugbyn", nullable = false)
    private Integer competitionUGBYN;

    @NotNull //Ajoutee
    @ManyToOne(fetch = FetchType.LAZY)
    private DisciplineSportive disciplineSportive;

    @NotNull //Ajoutee
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "region", "typeSelection", "lycee", "informationPersonnelle", "baccalaureat" }, allowSetters = true)
    private Etudiant etudiant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DisciplineSportiveEtudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLicenceSportiveYN() {
        return this.licenceSportiveYN;
    }

    public DisciplineSportiveEtudiant licenceSportiveYN(Integer licenceSportiveYN) {
        this.setLicenceSportiveYN(licenceSportiveYN);
        return this;
    }

    public void setLicenceSportiveYN(Integer licenceSportiveYN) {
        this.licenceSportiveYN = licenceSportiveYN;
    }

    public Integer getCompetitionUGBYN() {
        return this.competitionUGBYN;
    }

    public DisciplineSportiveEtudiant competitionUGBYN(Integer competitionUGBYN) {
        this.setCompetitionUGBYN(competitionUGBYN);
        return this;
    }

    public void setCompetitionUGBYN(Integer competitionUGBYN) {
        this.competitionUGBYN = competitionUGBYN;
    }

    public DisciplineSportive getDisciplineSportive() {
        return this.disciplineSportive;
    }

    public void setDisciplineSportive(DisciplineSportive disciplineSportive) {
        this.disciplineSportive = disciplineSportive;
    }

    public DisciplineSportiveEtudiant disciplineSportive(DisciplineSportive disciplineSportive) {
        this.setDisciplineSportive(disciplineSportive);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public DisciplineSportiveEtudiant etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisciplineSportiveEtudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((DisciplineSportiveEtudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisciplineSportiveEtudiant{" +
            "id=" + getId() +
            ", licenceSportiveYN=" + getLicenceSportiveYN() +
            ", competitionUGBYN=" + getCompetitionUGBYN() +
            "}";
    }
}
