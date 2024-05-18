package sn.ugb.gir.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeOperation.
 */
@Entity
@Table(name = "type_operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_type_operation")
    private String libelleTypeOperation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeOperation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeOperation() {
        return this.libelleTypeOperation;
    }

    public TypeOperation libelleTypeOperation(String libelleTypeOperation) {
        this.setLibelleTypeOperation(libelleTypeOperation);
        return this;
    }

    public void setLibelleTypeOperation(String libelleTypeOperation) {
        this.libelleTypeOperation = libelleTypeOperation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOperation)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeOperation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOperation{" +
            "id=" + getId() +
            ", libelleTypeOperation='" + getLibelleTypeOperation() + "'" +
            "}";
    }
}
