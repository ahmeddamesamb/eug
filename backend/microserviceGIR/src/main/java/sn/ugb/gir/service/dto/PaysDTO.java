package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Pays} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaysDTO implements Serializable {

    private Long id;

    @NotNull
    private String libellePays;

    private String paysEnAnglais;

    private String nationalite;

    private String codePays;

    private Boolean uEMOAYN;

    private Boolean cEDEAOYN;

    private Boolean rIMYN;

    private Boolean autreYN;

    private Boolean estEtrangerYN;

    private Set<ZoneDTO> zones = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibellePays() {
        return libellePays;
    }

    public void setLibellePays(String libellePays) {
        this.libellePays = libellePays;
    }

    public String getPaysEnAnglais() {
        return paysEnAnglais;
    }

    public void setPaysEnAnglais(String paysEnAnglais) {
        this.paysEnAnglais = paysEnAnglais;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getCodePays() {
        return codePays;
    }

    public void setCodePays(String codePays) {
        this.codePays = codePays;
    }

    public Boolean getuEMOAYN() {
        return uEMOAYN;
    }

    public void setuEMOAYN(Boolean uEMOAYN) {
        this.uEMOAYN = uEMOAYN;
    }

    public Boolean getcEDEAOYN() {
        return cEDEAOYN;
    }

    public void setcEDEAOYN(Boolean cEDEAOYN) {
        this.cEDEAOYN = cEDEAOYN;
    }

    public Boolean getrIMYN() {
        return rIMYN;
    }

    public void setrIMYN(Boolean rIMYN) {
        this.rIMYN = rIMYN;
    }

    public Boolean getAutreYN() {
        return autreYN;
    }

    public void setAutreYN(Boolean autreYN) {
        this.autreYN = autreYN;
    }

    public Boolean getEstEtrangerYN() {
        return estEtrangerYN;
    }

    public void setEstEtrangerYN(Boolean estEtrangerYN) {
        this.estEtrangerYN = estEtrangerYN;
    }

    public Set<ZoneDTO> getZones() {
        return zones;
    }

    public void setZones(Set<ZoneDTO> zones) {
        this.zones = zones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaysDTO)) {
            return false;
        }

        PaysDTO paysDTO = (PaysDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paysDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaysDTO{" +
            "id=" + getId() +
            ", libellePays='" + getLibellePays() + "'" +
            ", paysEnAnglais='" + getPaysEnAnglais() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", codePays='" + getCodePays() + "'" +
            ", uEMOAYN='" + getuEMOAYN() + "'" +
            ", cEDEAOYN='" + getcEDEAOYN() + "'" +
            ", rIMYN='" + getrIMYN() + "'" +
            ", autreYN='" + getAutreYN() + "'" +
            ", estEtrangerYN='" + getEstEtrangerYN() + "'" +
            ", zones=" + getZones() +
            "}";
    }
}
