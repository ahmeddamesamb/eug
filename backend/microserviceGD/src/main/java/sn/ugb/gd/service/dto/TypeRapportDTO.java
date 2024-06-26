package sn.ugb.gd.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gd.domain.TypeRapport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeRapportDTO implements Serializable {

    private Long id;

    private String libelleTypeRapport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeRapport() {
        return libelleTypeRapport;
    }

    public void setLibelleTypeRapport(String libelleTypeRapport) {
        this.libelleTypeRapport = libelleTypeRapport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeRapportDTO)) {
            return false;
        }

        TypeRapportDTO typeRapportDTO = (TypeRapportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeRapportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeRapportDTO{" +
            "id=" + getId() +
            ", libelleTypeRapport='" + getLibelleTypeRapport() + "'" +
            "}";
    }
}
