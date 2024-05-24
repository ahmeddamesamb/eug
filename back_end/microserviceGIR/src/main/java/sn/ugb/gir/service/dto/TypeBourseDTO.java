package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeBourse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeBourseDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleTypeBourse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeBourse() {
        return libelleTypeBourse;
    }

    public void setLibelleTypeBourse(String libelleTypeBourse) {
        this.libelleTypeBourse = libelleTypeBourse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeBourseDTO)) {
            return false;
        }

        TypeBourseDTO typeBourseDTO = (TypeBourseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeBourseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeBourseDTO{" +
            "id=" + getId() +
            ", libelleTypeBourse='" + getLibelleTypeBourse() + "'" +
            "}";
    }
}
