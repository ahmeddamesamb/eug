package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeSelection} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeSelectionDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleTypeSelection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeSelection() {
        return libelleTypeSelection;
    }

    public void setLibelleTypeSelection(String libelleTypeSelection) {
        this.libelleTypeSelection = libelleTypeSelection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSelectionDTO)) {
            return false;
        }

        TypeSelectionDTO typeSelectionDTO = (TypeSelectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeSelectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSelectionDTO{" +
            "id=" + getId() +
            ", libelleTypeSelection='" + getLibelleTypeSelection() + "'" +
            "}";
    }
}
