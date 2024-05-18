package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.FormationValide} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationValideDTO implements Serializable {

    private Long id;

    private Integer valideYN;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private FormationDTO formation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValideYN() {
        return valideYN;
    }

    public void setValideYN(Integer valideYN) {
        this.valideYN = valideYN;
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

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationValideDTO)) {
            return false;
        }

        FormationValideDTO formationValideDTO = (FormationValideDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formationValideDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationValideDTO{" +
            "id=" + getId() +
            ", valideYN=" + getValideYN() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", formation=" + getFormation() +
            "}";
    }
}
