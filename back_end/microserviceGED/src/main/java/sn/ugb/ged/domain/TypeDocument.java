package sn.ugb.ged.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeDocument.
 */
@Entity
@Table(name = "type_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_type_document")
    private String libelleTypeDocument;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeDocument() {
        return this.libelleTypeDocument;
    }

    public TypeDocument libelleTypeDocument(String libelleTypeDocument) {
        this.setLibelleTypeDocument(libelleTypeDocument);
        return this;
    }

    public void setLibelleTypeDocument(String libelleTypeDocument) {
        this.libelleTypeDocument = libelleTypeDocument;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDocument{" +
            "id=" + getId() +
            ", libelleTypeDocument='" + getLibelleTypeDocument() + "'" +
            "}";
    }
}
