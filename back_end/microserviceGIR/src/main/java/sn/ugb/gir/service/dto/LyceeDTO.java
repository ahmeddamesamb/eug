package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Lycee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LyceeDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomLycee;

    private String codeLycee;

    private String villeLycee;

    private Integer academieLycee;

    private String centreExamen;

    private RegionDTO region;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLycee() {
        return nomLycee;
    }

    public void setNomLycee(String nomLycee) {
        this.nomLycee = nomLycee;
    }

    public String getCodeLycee() {
        return codeLycee;
    }

    public void setCodeLycee(String codeLycee) {
        this.codeLycee = codeLycee;
    }

    public String getVilleLycee() {
        return villeLycee;
    }

    public void setVilleLycee(String villeLycee) {
        this.villeLycee = villeLycee;
    }

    public Integer getAcademieLycee() {
        return academieLycee;
    }

    public void setAcademieLycee(Integer academieLycee) {
        this.academieLycee = academieLycee;
    }

    public String getCentreExamen() {
        return centreExamen;
    }

    public void setCentreExamen(String centreExamen) {
        this.centreExamen = centreExamen;
    }

    public RegionDTO getRegion() {
        return region;
    }

    public void setRegion(RegionDTO region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LyceeDTO)) {
            return false;
        }

        LyceeDTO lyceeDTO = (LyceeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lyceeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LyceeDTO{" +
            "id=" + getId() +
            ", nomLycee='" + getNomLycee() + "'" +
            ", codeLycee='" + getCodeLycee() + "'" +
            ", villeLycee='" + getVilleLycee() + "'" +
            ", academieLycee=" + getAcademieLycee() +
            ", centreExamen='" + getCentreExamen() + "'" +
            ", region=" + getRegion() +
            "}";
    }
}
