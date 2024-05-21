package sn.ugb.ged.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.ged.domain.DocumentDelivre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDelivreDTO implements Serializable {

    private Long id;

    private String libelleDoc;

    private Instant anneeDoc;

    private Instant dateEnregistrement;

    private TypeDocumentDTO typeDocument;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleDoc() {
        return libelleDoc;
    }

    public void setLibelleDoc(String libelleDoc) {
        this.libelleDoc = libelleDoc;
    }

    public Instant getAnneeDoc() {
        return anneeDoc;
    }

    public void setAnneeDoc(Instant anneeDoc) {
        this.anneeDoc = anneeDoc;
    }

    public Instant getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Instant dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public TypeDocumentDTO getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocumentDTO typeDocument) {
        this.typeDocument = typeDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDelivreDTO)) {
            return false;
        }

        DocumentDelivreDTO documentDelivreDTO = (DocumentDelivreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDelivreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDelivreDTO{" +
            "id=" + getId() +
            ", libelleDoc='" + getLibelleDoc() + "'" +
            ", anneeDoc='" + getAnneeDoc() + "'" +
            ", dateEnregistrement='" + getDateEnregistrement() + "'" +
            ", typeDocument=" + getTypeDocument() +
            "}";
    }
}
