package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Mention} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MentionDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelleMention;

    private Boolean actifYN;

    private DomaineDTO domaine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleMention() {
        return libelleMention;
    }

    public void setLibelleMention(String libelleMention) {
        this.libelleMention = libelleMention;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public DomaineDTO getDomaine() {
        return domaine;
    }

    public void setDomaine(DomaineDTO domaine) {
        this.domaine = domaine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MentionDTO)) {
            return false;
        }

        MentionDTO mentionDTO = (MentionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mentionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MentionDTO{" +
            "id=" + getId() +
            ", libelleMention='" + getLibelleMention() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", domaine=" + getDomaine() +
            "}";
    }
}
