package sn.ugb.edt.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.edt.domain.Planning} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanningDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateDebut;

    @NotNull
    private Instant dateFin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningDTO)) {
            return false;
        }

        PlanningDTO planningDTO = (PlanningDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planningDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanningDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
