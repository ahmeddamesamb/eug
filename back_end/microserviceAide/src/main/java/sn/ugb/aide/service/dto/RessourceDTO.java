package sn.ugb.aide.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.aide.domain.Ressource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RessourceDTO implements Serializable {

    private Long id;

    private String nomRessource;

    private String descriptionRessource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomRessource() {
        return nomRessource;
    }

    public void setNomRessource(String nomRessource) {
        this.nomRessource = nomRessource;
    }

    public String getDescriptionRessource() {
        return descriptionRessource;
    }

    public void setDescriptionRessource(String descriptionRessource) {
        this.descriptionRessource = descriptionRessource;
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
            ", nomRessource='" + getNomRessource() + "'" +
            ", descriptionRessource='" + getDescriptionRessource() + "'" +
            "}";
    }
}
