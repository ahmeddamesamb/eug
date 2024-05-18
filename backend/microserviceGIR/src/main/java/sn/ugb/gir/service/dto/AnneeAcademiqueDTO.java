package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.AnneeAcademique} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnneeAcademiqueDTO implements Serializable {

    private Long id;

    private String libelleAnneeAcademique;

    private Integer anneeCourante;

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

    public Integer getAnneeCourante() {
        return anneeCourante;
    }

    public void setAnneeCourante(Integer anneeCourante) {
        this.anneeCourante = anneeCourante;
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
            ", anneeCourante=" + getAnneeCourante() +
            "}";
    }
}
