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

    private LocalDate dateDebuT;

    private LocalDate dateFin;

    private Integer enCours;

    private Set<FormationDTO> formations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebuT() {
        return dateDebuT;
    }

    public void setDateDebuT(LocalDate dateDebuT) {
        this.dateDebuT = dateDebuT;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getEnCours() {
        return enCours;
    }

    public void setEnCours(Integer enCours) {
        this.enCours = enCours;
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
            ", dateDebuT='" + getDateDebuT() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCours=" + getEnCours() +
            ", formations=" + getFormations() +
            "}";
    }
}
