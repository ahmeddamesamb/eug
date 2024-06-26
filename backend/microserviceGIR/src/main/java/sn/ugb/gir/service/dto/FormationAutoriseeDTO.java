package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link sn.ugb.gir.domain.FormationAutorisee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationAutoriseeDTO implements Serializable {

    private Long id;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Boolean enCoursYN;

    private Boolean actifYN;

    private Set<FormationDTO> formations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getEnCoursYN() {
        return enCoursYN;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<FormationDTO> getFormations() {
        return formations;
    }

    public void setFormations(Set<FormationDTO> formations) {
        this.formations = formations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationAutoriseeDTO)) {
            return false;
        }

        FormationAutoriseeDTO formationAutoriseeDTO = (FormationAutoriseeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formationAutoriseeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationAutoriseeDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", formations=" + getFormations() +
            "}";
    }
}
