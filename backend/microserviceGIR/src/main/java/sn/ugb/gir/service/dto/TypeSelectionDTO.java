package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeSelection} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeSelectionDTO implements Serializable {

    private Long id;

    private String libelle;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSelectionDTO)) {
            return false;
        }

        TypeSelectionDTO typeSelectionDTO = (TypeSelectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeSelectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSelectionDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
