package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Domaine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DomaineDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleDomaine;

    private Boolean actifYN;

    private Set<UfrDTO> ufrs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleDomaine() {
        return libelleDomaine;
    }

    public void setLibelleDomaine(String libelleDomaine) {
        this.libelleDomaine = libelleDomaine;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<UfrDTO> getUfrs() {
        return ufrs;
    }

    public void setUfrs(Set<UfrDTO> ufrs) {
        this.ufrs = ufrs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomaineDTO)) {
            return false;
        }

        DomaineDTO domaineDTO = (DomaineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, domaineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DomaineDTO{" +
            "id=" + getId() +
            ", libelleDomaine='" + getLibelleDomaine() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", ufrs=" + getUfrs() +
            "}";
    }
}
