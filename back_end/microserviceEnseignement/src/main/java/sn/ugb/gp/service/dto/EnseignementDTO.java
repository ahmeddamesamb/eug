package sn.ugb.gp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gp.domain.Enseignement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnseignementDTO implements Serializable {

    private Long id;

    private String libelleEnseignements;

    private Float volumeHoraire;

    private Integer nombreInscrits;

    private Integer groupeYN;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleEnseignements() {
        return libelleEnseignements;
    }

    public void setLibelleEnseignements(String libelleEnseignements) {
        this.libelleEnseignements = libelleEnseignements;
    }

    public Float getVolumeHoraire() {
        return volumeHoraire;
    }

    public void setVolumeHoraire(Float volumeHoraire) {
        this.volumeHoraire = volumeHoraire;
    }

    public Integer getNombreInscrits() {
        return nombreInscrits;
    }

    public void setNombreInscrits(Integer nombreInscrits) {
        this.nombreInscrits = nombreInscrits;
    }

    public Integer getGroupeYN() {
        return groupeYN;
    }

    public void setGroupeYN(Integer groupeYN) {
        this.groupeYN = groupeYN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnseignementDTO)) {
            return false;
        }

        EnseignementDTO enseignementDTO = (EnseignementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enseignementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnseignementDTO{" +
            "id=" + getId() +
            ", libelleEnseignements='" + getLibelleEnseignements() + "'" +
            ", volumeHoraire=" + getVolumeHoraire() +
            ", nombreInscrits=" + getNombreInscrits() +
            ", groupeYN=" + getGroupeYN() +
            "}";
    }
}
