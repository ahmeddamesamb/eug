package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Serie} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SerieDTO implements Serializable {

    private Long id;

    private String codeSerie;

    private String libelleSerie;

    private String sigleSerie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeSerie() {
        return codeSerie;
    }

    public void setCodeSerie(String codeSerie) {
        this.codeSerie = codeSerie;
    }

    public String getLibelleSerie() {
        return libelleSerie;
    }

    public void setLibelleSerie(String libelleSerie) {
        this.libelleSerie = libelleSerie;
    }

    public String getSigleSerie() {
        return sigleSerie;
    }

    public void setSigleSerie(String sigleSerie) {
        this.sigleSerie = sigleSerie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SerieDTO)) {
            return false;
        }

        SerieDTO serieDTO = (SerieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SerieDTO{" +
            "id=" + getId() +
            ", codeSerie='" + getCodeSerie() + "'" +
            ", libelleSerie='" + getLibelleSerie() + "'" +
            ", sigleSerie='" + getSigleSerie() + "'" +
            "}";
    }
}
