package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeHandicap} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeHandicapDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleTypeHandicap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeHandicap() {
        return libelleTypeHandicap;
    }

    public void setLibelleTypeHandicap(String libelleTypeHandicap) {
        this.libelleTypeHandicap = libelleTypeHandicap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeHandicapDTO)) {
            return false;
        }

        TypeHandicapDTO typeHandicapDTO = (TypeHandicapDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeHandicapDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeHandicapDTO{" +
            "id=" + getId() +
            ", libelleTypeHandicap='" + getLibelleTypeHandicap() + "'" +
            "}";
    }
}
