package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Niveau} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NiveauDTO implements Serializable {

    private Long id;

    private String libelleNiveau;

    private String cycleNiveau;

    private String codeNiveau;

    private String anneeEtude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleNiveau() {
        return libelleNiveau;
    }

    public void setLibelleNiveau(String libelleNiveau) {
        this.libelleNiveau = libelleNiveau;
    }

    public String getCycleNiveau() {
        return cycleNiveau;
    }

    public void setCycleNiveau(String cycleNiveau) {
        this.cycleNiveau = cycleNiveau;
    }

    public String getCodeNiveau() {
        return codeNiveau;
    }

    public void setCodeNiveau(String codeNiveau) {
        this.codeNiveau = codeNiveau;
    }

    public String getAnneeEtude() {
        return anneeEtude;
    }

    public void setAnneeEtude(String anneeEtude) {
        this.anneeEtude = anneeEtude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NiveauDTO)) {
            return false;
        }

        NiveauDTO niveauDTO = (NiveauDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, niveauDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NiveauDTO{" +
            "id=" + getId() +
            ", libelleNiveau='" + getLibelleNiveau() + "'" +
            ", cycleNiveau='" + getCycleNiveau() + "'" +
            ", codeNiveau='" + getCodeNiveau() + "'" +
            ", anneeEtude='" + getAnneeEtude() + "'" +
            "}";
    }
}
