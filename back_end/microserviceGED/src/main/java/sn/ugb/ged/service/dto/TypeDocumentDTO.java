package sn.ugb.ged.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.ged.domain.TypeDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDocumentDTO implements Serializable {

    private Long id;

    private String libelleTypeDocument;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeDocument() {
        return libelleTypeDocument;
    }

    public void setLibelleTypeDocument(String libelleTypeDocument) {
        this.libelleTypeDocument = libelleTypeDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDocumentDTO)) {
            return false;
        }

        TypeDocumentDTO typeDocumentDTO = (TypeDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDocumentDTO{" +
            "id=" + getId() +
            ", libelleTypeDocument='" + getLibelleTypeDocument() + "'" +
            "}";
    }
}
