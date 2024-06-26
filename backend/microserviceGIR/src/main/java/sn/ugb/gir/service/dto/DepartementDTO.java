package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Departement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartementDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomDepatement;

    private Boolean actifYN;

    private UfrDTO ufr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDepatement() {
        return nomDepatement;
    }

    public void setNomDepatement(String nomDepatement) {
        this.nomDepatement = nomDepatement;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public UfrDTO getUfr() {
        return ufr;
    }

    public void setUfr(UfrDTO ufr) {
        this.ufr = ufr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartementDTO)) {
            return false;
        }

        DepartementDTO departementDTO = (DepartementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementDTO{" +
            "id=" + getId() +
            ", nomDepatement='" + getNomDepatement() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", ufr=" + getUfr() +
            "}";
    }
}
