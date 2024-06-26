package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Universite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniversiteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomUniversite;

    @NotNull
    private String sigleUniversite;

    @NotNull
    private Boolean actifYN;

    private MinistereDTO ministere;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUniversite() {
        return nomUniversite;
    }

    public void setNomUniversite(String nomUniversite) {
        this.nomUniversite = nomUniversite;
    }

    public String getSigleUniversite() {
        return sigleUniversite;
    }

    public void setSigleUniversite(String sigleUniversite) {
        this.sigleUniversite = sigleUniversite;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public MinistereDTO getMinistere() {
        return ministere;
    }

    public void setMinistere(MinistereDTO ministere) {
        this.ministere = ministere;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniversiteDTO)) {
            return false;
        }

        UniversiteDTO universiteDTO = (UniversiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, universiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniversiteDTO{" +
            "id=" + getId() +
            ", nomUniversite='" + getNomUniversite() + "'" +
            ", sigleUniversite='" + getSigleUniversite() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", ministere=" + getMinistere() +
            "}";
    }
}
