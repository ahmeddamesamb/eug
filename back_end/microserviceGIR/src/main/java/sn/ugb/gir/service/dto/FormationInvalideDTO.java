package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.FormationInvalide} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationInvalideDTO implements Serializable {

    private Long id;

    private Integer actifYN;

    private FormationDTO formation;

    private AnneeAcademiqueDTO anneAcademique;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActifYN() {
        return actifYN;
    }

    public void setActifYN(Integer actifYN) {
        this.actifYN = actifYN;
    }

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    public AnneeAcademiqueDTO getAnneAcademique() {
        return anneAcademique;
    }

    public void setAnneAcademique(AnneeAcademiqueDTO anneAcademique) {
        this.anneAcademique = anneAcademique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationInvalideDTO)) {
            return false;
        }

        FormationInvalideDTO formationInvalideDTO = (FormationInvalideDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formationInvalideDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationInvalideDTO{" +
            "id=" + getId() +
            ", actifYN=" + getActifYN() +
            ", formation=" + getFormation() +
            ", anneAcademique=" + getAnneAcademique() +
            "}";
    }
}
