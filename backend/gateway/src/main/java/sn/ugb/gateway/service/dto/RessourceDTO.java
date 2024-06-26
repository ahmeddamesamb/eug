package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.Ressource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RessourceDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String libelle;

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
        if (!(o instanceof RessourceDTO)) {
            return false;
        }

        RessourceDTO ressourceDTO = (RessourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ressourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RessourceDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
