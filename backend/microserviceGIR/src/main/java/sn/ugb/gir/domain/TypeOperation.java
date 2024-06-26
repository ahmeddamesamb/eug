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
 * A TypeOperation.
 */
@Entity
@Table(name = "type_operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "typeoperation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_type_operation", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleTypeOperation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeOperation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "typeOperation" }, allowSetters = true)
    private Set<Operation> operations = new HashSet<>();

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

    public Set<Operation> getOperations() {
        return this.operations;
    }

    public void setOperations(Set<Operation> operations) {
        if (this.operations != null) {
            this.operations.forEach(i -> i.setTypeOperation(null));
        }
        if (operations != null) {
            operations.forEach(i -> i.setTypeOperation(this));
        }
        this.operations = operations;
    }

    public TypeOperation operations(Set<Operation> operations) {
        this.setOperations(operations);
        return this;
    }

    public TypeOperation addOperations(Operation operation) {
        this.operations.add(operation);
        operation.setTypeOperation(this);
        return this;
    }

    public TypeOperation removeOperations(Operation operation) {
        this.operations.remove(operation);
        operation.setTypeOperation(null);
        return this;
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
