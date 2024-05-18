package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeOperation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeOperationDTO implements Serializable {

    private Long id;

    private String libelleTypeOperation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeOperation() {
        return libelleTypeOperation;
    }

    public void setLibelleTypeOperation(String libelleTypeOperation) {
        this.libelleTypeOperation = libelleTypeOperation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOperationDTO)) {
            return false;
        }

        TypeOperationDTO typeOperationDTO = (TypeOperationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeOperationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOperationDTO{" +
            "id=" + getId() +
            ", libelleTypeOperation='" + getLibelleTypeOperation() + "'" +
            "}";
    }
}
