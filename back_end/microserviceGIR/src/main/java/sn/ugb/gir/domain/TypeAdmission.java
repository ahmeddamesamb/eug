package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeAdmission.
 */
@Entity
@Table(name = "type_admission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeAdmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_type_admission", nullable = false, unique = true)
    private String libelleTypeAdmission;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeAdmission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeAdmission() {
        return this.libelleTypeAdmission;
    }

    public TypeAdmission libelleTypeAdmission(String libelleTypeAdmission) {
        this.setLibelleTypeAdmission(libelleTypeAdmission);
        return this;
    }

    public void setLibelleTypeAdmission(String libelleTypeAdmission) {
        this.libelleTypeAdmission = libelleTypeAdmission;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeAdmission)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeAdmission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeAdmission{" +
            "id=" + getId() +
            ", libelleTypeAdmission='" + getLibelleTypeAdmission() + "'" +
            "}";
    }
}
