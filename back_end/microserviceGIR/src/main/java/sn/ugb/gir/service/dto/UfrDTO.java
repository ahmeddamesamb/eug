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
    private String libeleUfr;

    @NotNull
    private String sigleUfr;

    @NotNull
    private Integer systemeLMDYN;

    private Integer ordreStat;

    private UniversiteDTO universite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleUfr() {
        return libeleUfr;
    }

    public void setLibeleUfr(String libeleUfr) {
        this.libeleUfr = libeleUfr;
    }

    public String getSigleUfr() {
        return sigleUfr;
    }

    public void setSigleUfr(String sigleUfr) {
        this.sigleUfr = sigleUfr;
    }

    public Integer getSystemeLMDYN() {
        return systemeLMDYN;
    }

    public void setSystemeLMDYN(Integer systemeLMDYN) {
        this.systemeLMDYN = systemeLMDYN;
    }

    public Integer getOrdreStat() {
        return ordreStat;
    }

    public void setOrdreStat(Integer ordreStat) {
        this.ordreStat = ordreStat;
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
            ", libeleUfr='" + getLibeleUfr() + "'" +
            ", sigleUfr='" + getSigleUfr() + "'" +
            ", systemeLMDYN=" + getSystemeLMDYN() +
            ", ordreStat=" + getOrdreStat() +
            ", universite=" + getUniversite() +
            "}";
    }
}
