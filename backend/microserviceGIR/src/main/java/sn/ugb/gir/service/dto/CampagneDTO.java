package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Campagne} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CampagneDTO implements Serializable {

    private Long id;

    private String libelleCampagne;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String libelleAbrege;

    private Boolean actifYN;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleCampagne() {
        return libelleCampagne;
    }

    public void setLibelleCampagne(String libelleCampagne) {
        this.libelleCampagne = libelleCampagne;
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

    public String getLibelleAbrege() {
        return libelleAbrege;
    }

    public void setLibelleAbrege(String libelleAbrege) {
        this.libelleAbrege = libelleAbrege;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampagneDTO)) {
            return false;
        }

        CampagneDTO campagneDTO = (CampagneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, campagneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CampagneDTO{" +
            "id=" + getId() +
            ", libelleCampagne='" + getLibelleCampagne() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", libelleAbrege='" + getLibelleAbrege() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
