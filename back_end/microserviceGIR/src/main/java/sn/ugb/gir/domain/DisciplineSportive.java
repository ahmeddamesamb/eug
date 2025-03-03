package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DisciplineSportive.
 */
@Entity
@Table(name = "discipline_sportive")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private String libelleDisciplineSportive;

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
