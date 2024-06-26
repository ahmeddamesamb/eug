package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Region} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegionDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleRegion;

    private PaysDTO pays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleRegion() {
        return libelleRegion;
    }

    public void setLibelleRegion(String libelleRegion) {
        this.libelleRegion = libelleRegion;
    }

    public PaysDTO getPays() {
        return pays;
    }

    public void setPays(PaysDTO pays) {
        this.pays = pays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegionDTO)) {
            return false;
        }

        RegionDTO regionDTO = (RegionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, regionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionDTO{" +
            "id=" + getId() +
            ", libelleRegion='" + getLibelleRegion() + "'" +
            ", pays=" + getPays() +
            "}";
    }
}
