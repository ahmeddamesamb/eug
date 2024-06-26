package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.TypeFrais} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeFraisDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleTypeFrais;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeFrais() {
        return libelleTypeFrais;
    }

    public void setLibelleTypeFrais(String libelleTypeFrais) {
        this.libelleTypeFrais = libelleTypeFrais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeFraisDTO)) {
            return false;
        }

        TypeFraisDTO typeFraisDTO = (TypeFraisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeFraisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeFraisDTO{" +
            "id=" + getId() +
            ", libelleTypeFrais='" + getLibelleTypeFrais() + "'" +
            "}";
    }
}
