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
    @Min(value = 1990)
    @Max(value = 2060)
    private Integer anneeAc;

    @NotNull
    @Size(max = 1)
    private String separateur;

    private Boolean anneeCouranteYN;

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

    public Integer getAnneeAc() {
        return anneeAc;
    }

    public void setAnneeAc(Integer anneeAc) {
        this.anneeAc = anneeAc;
    }

    public String getSeparateur() {
        return separateur;
    }

    public void setSeparateur(String separateur) {
        this.separateur = separateur;
    }

    public Boolean getAnneeCouranteYN() {
        return anneeCouranteYN;
    }

    public void setAnneeCouranteYN(Boolean anneeCouranteYN) {
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
            ", anneeAc=" + getAnneeAc() +
            ", separateur='" + getSeparateur() + "'" +
            ", anneeCouranteYN='" + getAnneeCouranteYN() + "'" +
            "}";
    }
}
