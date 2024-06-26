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
 * A DisciplineSportive.
 */
@Entity
@Table(name = "discipline_sportive")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "disciplinesportive")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisciplineSportive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_discipline_sportive", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleDisciplineSportive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "disciplineSportive")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "disciplineSportive", "etudiant" }, allowSetters = true)
    private Set<DisciplineSportiveEtudiant> disciplineSportiveEtudiants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DisciplineSportive id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleDisciplineSportive() {
        return this.libelleDisciplineSportive;
    }

    public DisciplineSportive libelleDisciplineSportive(String libelleDisciplineSportive) {
        this.setLibelleDisciplineSportive(libelleDisciplineSportive);
        return this;
    }

    public void setLibelleDisciplineSportive(String libelleDisciplineSportive) {
        this.libelleDisciplineSportive = libelleDisciplineSportive;
    }

    public Set<DisciplineSportiveEtudiant> getDisciplineSportiveEtudiants() {
        return this.disciplineSportiveEtudiants;
    }

    public void setDisciplineSportiveEtudiants(Set<DisciplineSportiveEtudiant> disciplineSportiveEtudiants) {
        if (this.disciplineSportiveEtudiants != null) {
            this.disciplineSportiveEtudiants.forEach(i -> i.setDisciplineSportive(null));
        }
        if (disciplineSportiveEtudiants != null) {
            disciplineSportiveEtudiants.forEach(i -> i.setDisciplineSportive(this));
        }
        this.disciplineSportiveEtudiants = disciplineSportiveEtudiants;
    }

    public DisciplineSportive disciplineSportiveEtudiants(Set<DisciplineSportiveEtudiant> disciplineSportiveEtudiants) {
        this.setDisciplineSportiveEtudiants(disciplineSportiveEtudiants);
        return this;
    }

    public DisciplineSportive addDisciplineSportiveEtudiants(DisciplineSportiveEtudiant disciplineSportiveEtudiant) {
        this.disciplineSportiveEtudiants.add(disciplineSportiveEtudiant);
        disciplineSportiveEtudiant.setDisciplineSportive(this);
        return this;
    }

    public DisciplineSportive removeDisciplineSportiveEtudiants(DisciplineSportiveEtudiant disciplineSportiveEtudiant) {
        this.disciplineSportiveEtudiants.remove(disciplineSportiveEtudiant);
        disciplineSportiveEtudiant.setDisciplineSportive(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisciplineSportive)) {
            return false;
        }
        return getId() != null && getId().equals(((DisciplineSportive) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisciplineSportive{" +
            "id=" + getId() +
            ", libelleDisciplineSportive='" + getLibelleDisciplineSportive() + "'" +
            "}";
    }
}
