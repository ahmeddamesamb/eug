package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.Profil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfilDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String libelle;

    @NotNull(message = "must not be null")
    private LocalDate dateAjout;

    @NotNull(message = "must not be null")
    private Boolean actifYN;

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

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
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
        if (!(o instanceof ProfilDTO)) {
            return false;
        }

        ProfilDTO profilDTO = (ProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateAjout='" + getDateAjout() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
