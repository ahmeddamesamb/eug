package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Cycle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CycleDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleCycle() {
        return libelleCycle;
    }

    public void setLibelleCycle(String libelleCycle) {
        this.libelleCycle = libelleCycle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CycleDTO)) {
            return false;
        }

        CycleDTO cycleDTO = (CycleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cycleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDTO{" +
            "id=" + getId() +
            ", libelleCycle='" + getLibelleCycle() + "'" +
            "}";
    }
}
