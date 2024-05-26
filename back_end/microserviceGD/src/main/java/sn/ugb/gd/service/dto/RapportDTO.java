package sn.ugb.gd.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gd.domain.Rapport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportDTO implements Serializable {

    private Long id;

    private String libelleRapport;

    private String descriptionRapport;

    private String contenuRapport;

    private Instant dateRedaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleRapport() {
        return libelleRapport;
    }

    public void setLibelleRapport(String libelleRapport) {
        this.libelleRapport = libelleRapport;
    }

    public String getDescriptionRapport() {
        return descriptionRapport;
    }

    public void setDescriptionRapport(String descriptionRapport) {
        this.descriptionRapport = descriptionRapport;
    }

    public String getContenuRapport() {
        return contenuRapport;
    }

    public void setContenuRapport(String contenuRapport) {
        this.contenuRapport = contenuRapport;
    }

    public Instant getDateRedaction() {
        return dateRedaction;
    }

    public void setDateRedaction(Instant dateRedaction) {
        this.dateRedaction = dateRedaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportDTO)) {
            return false;
        }

        RapportDTO rapportDTO = (RapportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rapportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportDTO{" +
            "id=" + getId() +
            ", libelleRapport='" + getLibelleRapport() + "'" +
            ", descriptionRapport='" + getDescriptionRapport() + "'" +
            ", contenuRapport='" + getContenuRapport() + "'" +
            ", dateRedaction='" + getDateRedaction() + "'" +
            "}";
    }
}
