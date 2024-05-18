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

    private String libelle;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String libelleAbrege;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
            ", libelle='" + getLibelle() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", libelleAbrege='" + getLibelleAbrege() + "'" +
            "}";
    }
}
