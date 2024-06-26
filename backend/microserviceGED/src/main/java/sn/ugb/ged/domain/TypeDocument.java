package sn.ugb.ged.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeDocument.
 */
@Entity
@Table(name = "type_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "typedocument")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_type_document")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleTypeDocument;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeDocument")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "typeDocument" }, allowSetters = true)
    private Set<DocumentDelivre> documentsDelivres = new HashSet<>();

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

    public Set<DocumentDelivre> getDocumentsDelivres() {
        return this.documentsDelivres;
    }

    public void setDocumentsDelivres(Set<DocumentDelivre> documentDelivres) {
        if (this.documentsDelivres != null) {
            this.documentsDelivres.forEach(i -> i.setTypeDocument(null));
        }
        if (documentDelivres != null) {
            documentDelivres.forEach(i -> i.setTypeDocument(this));
        }
        this.documentsDelivres = documentDelivres;
    }

    public TypeDocument documentsDelivres(Set<DocumentDelivre> documentDelivres) {
        this.setDocumentsDelivres(documentDelivres);
        return this;
    }

    public TypeDocument addDocumentsDelivres(DocumentDelivre documentDelivre) {
        this.documentsDelivres.add(documentDelivre);
        documentDelivre.setTypeDocument(this);
        return this;
    }

    public TypeDocument removeDocumentsDelivres(DocumentDelivre documentDelivre) {
        this.documentsDelivres.remove(documentDelivre);
        documentDelivre.setTypeDocument(null);
        return this;
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
