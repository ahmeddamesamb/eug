package sn.ugb.ged.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentDelivre.
 */
@Entity
@Table(name = "document_delivre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDelivre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_doc")
    private String libelleDoc;

    @Column(name = "annee_doc")
    private Instant anneeDoc;

    @Column(name = "date_enregistrement")
    private Instant dateEnregistrement;

    @ManyToOne(fetch = FetchType.LAZY)
    private TypeDocument typeDocument;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentDelivre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleDoc() {
        return this.libelleDoc;
    }

    public DocumentDelivre libelleDoc(String libelleDoc) {
        this.setLibelleDoc(libelleDoc);
        return this;
    }

    public void setLibelleDoc(String libelleDoc) {
        this.libelleDoc = libelleDoc;
    }

    public Instant getAnneeDoc() {
        return this.anneeDoc;
    }

    public DocumentDelivre anneeDoc(Instant anneeDoc) {
        this.setAnneeDoc(anneeDoc);
        return this;
    }

    public void setAnneeDoc(Instant anneeDoc) {
        this.anneeDoc = anneeDoc;
    }

    public Instant getDateEnregistrement() {
        return this.dateEnregistrement;
    }

    public DocumentDelivre dateEnregistrement(Instant dateEnregistrement) {
        this.setDateEnregistrement(dateEnregistrement);
        return this;
    }

    public void setDateEnregistrement(Instant dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public TypeDocument getTypeDocument() {
        return this.typeDocument;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }

    public DocumentDelivre typeDocument(TypeDocument typeDocument) {
        this.setTypeDocument(typeDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDelivre)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentDelivre) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDelivre{" +
            "id=" + getId() +
            ", libelleDoc='" + getLibelleDoc() + "'" +
            ", anneeDoc='" + getAnneeDoc() + "'" +
            ", dateEnregistrement='" + getDateEnregistrement() + "'" +
            "}";
    }
}
