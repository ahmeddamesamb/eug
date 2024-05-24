package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.AnneeAcademique} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnneeAcademiqueDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleAnneeAcademique;

    @NotNull
    @Size(max = 4)
    private String anneeAc;

    private Integer anneeCouranteYN;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleAnneeAcademique() {
        return libelleAnneeAcademique;
    }

    public void setLibelleAnneeAcademique(String libelleAnneeAcademique) {
        this.libelleAnneeAcademique = libelleAnneeAcademique;
    }

    public String getAnneeAc() {
        return anneeAc;
    }

    public void setAnneeAc(String anneeAc) {
        this.anneeAc = anneeAc;
    }

    public Integer getAnneeCouranteYN() {
        return anneeCouranteYN;
    }

    public void setAnneeCouranteYN(Integer anneeCouranteYN) {
        this.anneeCouranteYN = anneeCouranteYN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnneeAcademiqueDTO)) {
            return false;
        }

        AnneeAcademiqueDTO anneeAcademiqueDTO = (AnneeAcademiqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, anneeAcademiqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnneeAcademiqueDTO{" +
            "id=" + getId() +
            ", libelleAnneeAcademique='" + getLibelleAnneeAcademique() + "'" +
            ", anneeAc='" + getAnneeAc() + "'" +
            ", anneeCouranteYN=" + getAnneeCouranteYN() +
            "}";
    }
}
