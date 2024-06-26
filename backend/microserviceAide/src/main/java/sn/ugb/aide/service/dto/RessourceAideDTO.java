package sn.ugb.aide.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.aide.domain.RessourceAide} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RessourceAideDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String libelle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RessourceAideDTO)) {
            return false;
        }

        RessourceAideDTO ressourceAideDTO = (RessourceAideDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ressourceAideDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RessourceAideDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
