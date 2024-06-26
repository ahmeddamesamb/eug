package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeAdmission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeAdmissionDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleTypeAdmission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeAdmission() {
        return libelleTypeAdmission;
    }

    public void setLibelleTypeAdmission(String libelleTypeAdmission) {
        this.libelleTypeAdmission = libelleTypeAdmission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeAdmissionDTO)) {
            return false;
        }

        TypeAdmissionDTO typeAdmissionDTO = (TypeAdmissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeAdmissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeAdmissionDTO{" +
            "id=" + getId() +
            ", libelleTypeAdmission='" + getLibelleTypeAdmission() + "'" +
            "}";
    }
}
