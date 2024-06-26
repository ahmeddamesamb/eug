package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.DisciplineSportive} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisciplineSportiveDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleDisciplineSportive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleDisciplineSportive() {
        return libelleDisciplineSportive;
    }

    public void setLibelleDisciplineSportive(String libelleDisciplineSportive) {
        this.libelleDisciplineSportive = libelleDisciplineSportive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisciplineSportiveDTO)) {
            return false;
        }

        DisciplineSportiveDTO disciplineSportiveDTO = (DisciplineSportiveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, disciplineSportiveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisciplineSportiveDTO{" +
            "id=" + getId() +
            ", libelleDisciplineSportive='" + getLibelleDisciplineSportive() + "'" +
            "}";
    }
}
