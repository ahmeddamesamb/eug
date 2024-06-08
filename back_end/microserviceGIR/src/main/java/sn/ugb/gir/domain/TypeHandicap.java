package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeHandicap.
 */
@Entity
@Table(name = "type_handicap")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeHandicap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    //@Pattern(regexp = "^[A-Z][a-z]*$", message = "Le libellé doit commencer par une majuscule et le reste doit être en minuscules.")
    @Column(name = "libelle_type_handicap", nullable = false, unique = true)
    private String libelleTypeHandicap;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeHandicap id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeHandicap() {
        return this.libelleTypeHandicap;
    }

    public TypeHandicap libelleTypeHandicap(String libelleTypeHandicap) {
        this.setLibelleTypeHandicap(libelleTypeHandicap);
        return this;
    }

    public void setLibelleTypeHandicap(String libelleTypeHandicap) {
        this.libelleTypeHandicap = libelleTypeHandicap;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeHandicap)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeHandicap) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeHandicap{" +
            "id=" + getId() +
            ", libelleTypeHandicap='" + getLibelleTypeHandicap() + "'" +
            "}";
    }
}
