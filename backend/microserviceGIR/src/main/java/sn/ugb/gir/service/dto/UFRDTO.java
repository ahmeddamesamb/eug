package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.UFR} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UFRDTO implements Serializable {

    private Long id;

    private String libeleUFR;

    private String sigleUFR;

    private Integer systemeLMDYN;

    private Integer ordreStat;

    private UniversiteDTO universite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleUFR() {
        return libeleUFR;
    }

    public void setLibeleUFR(String libeleUFR) {
        this.libeleUFR = libeleUFR;
    }

    public String getSigleUFR() {
        return sigleUFR;
    }

    public void setSigleUFR(String sigleUFR) {
        this.sigleUFR = sigleUFR;
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
        if (!(o instanceof UFRDTO)) {
            return false;
        }

        UFRDTO uFRDTO = (UFRDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uFRDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UFRDTO{" +
            "id=" + getId() +
            ", libeleUFR='" + getLibeleUFR() + "'" +
            ", sigleUFR='" + getSigleUFR() + "'" +
            ", systemeLMDYN=" + getSystemeLMDYN() +
            ", ordreStat=" + getOrdreStat() +
            ", universite=" + getUniversite() +
            "}";
    }
}
