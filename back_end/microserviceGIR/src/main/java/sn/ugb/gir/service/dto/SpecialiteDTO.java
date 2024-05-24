package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Specialite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialiteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomSpecialites;

    @NotNull
    private String sigleSpecialites;

    private Integer specialiteParticulierYN;

    @NotNull
    private Integer specialitesPayanteYN;

    private MentionDTO mention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSpecialites() {
        return nomSpecialites;
    }

    public void setNomSpecialites(String nomSpecialites) {
        this.nomSpecialites = nomSpecialites;
    }

    public String getSigleSpecialites() {
        return sigleSpecialites;
    }

    public void setSigleSpecialites(String sigleSpecialites) {
        this.sigleSpecialites = sigleSpecialites;
    }

    public Integer getSpecialiteParticulierYN() {
        return specialiteParticulierYN;
    }

    public void setSpecialiteParticulierYN(Integer specialiteParticulierYN) {
        this.specialiteParticulierYN = specialiteParticulierYN;
    }

    public Integer getSpecialitesPayanteYN() {
        return specialitesPayanteYN;
    }

    public void setSpecialitesPayanteYN(Integer specialitesPayanteYN) {
        this.specialitesPayanteYN = specialitesPayanteYN;
    }

    public MentionDTO getMention() {
        return mention;
    }

    public void setMention(MentionDTO mention) {
        this.mention = mention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialiteDTO)) {
            return false;
        }

        SpecialiteDTO specialiteDTO = (SpecialiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, specialiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialiteDTO{" +
            "id=" + getId() +
            ", nomSpecialites='" + getNomSpecialites() + "'" +
            ", sigleSpecialites='" + getSigleSpecialites() + "'" +
            ", specialiteParticulierYN=" + getSpecialiteParticulierYN() +
            ", specialitesPayanteYN=" + getSpecialitesPayanteYN() +
            ", mention=" + getMention() +
            "}";
    }
}
