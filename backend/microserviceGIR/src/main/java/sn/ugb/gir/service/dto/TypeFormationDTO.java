package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeFormation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeFormationDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleTypeFormation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeFormation() {
        return libelleTypeFormation;
    }

    public void setLibelleTypeFormation(String libelleTypeFormation) {
        this.libelleTypeFormation = libelleTypeFormation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeFormationDTO)) {
            return false;
        }

        TypeFormationDTO typeFormationDTO = (TypeFormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeFormationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeFormationDTO{" +
            "id=" + getId() +
            ", libelleTypeFormation='" + getLibelleTypeFormation() + "'" +
            "}";
    }
}
