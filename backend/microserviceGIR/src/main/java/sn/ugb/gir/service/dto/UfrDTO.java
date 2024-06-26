package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Ufr} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UfrDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleUfr;

    @NotNull
    private String sigleUfr;

    private String prefixe;

    private Boolean actifYN;

    private UniversiteDTO universite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleUfr() {
        return libelleUfr;
    }

    public void setLibelleUfr(String libelleUfr) {
        this.libelleUfr = libelleUfr;
    }

    public String getSigleUfr() {
        return sigleUfr;
    }

    public void setSigleUfr(String sigleUfr) {
        this.sigleUfr = sigleUfr;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public UniversiteDTO getUniversite() {
        return universite;
    }

    public void setUniversite(UniversiteDTO universite) {
        this.universite = universite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UfrDTO)) {
            return false;
        }

        UfrDTO ufrDTO = (UfrDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ufrDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UfrDTO{" +
            "id=" + getId() +
            ", libelleUfr='" + getLibelleUfr() + "'" +
            ", sigleUfr='" + getSigleUfr() + "'" +
            ", prefixe='" + getPrefixe() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", universite=" + getUniversite() +
            "}";
    }
}
