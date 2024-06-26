package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.InformationImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationImageDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private String cheminPath;

    @NotNull
    private String cheminFile;

    private String enCoursYN;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheminPath() {
        return cheminPath;
    }

    public void setCheminPath(String cheminPath) {
        this.cheminPath = cheminPath;
    }

    public String getCheminFile() {
        return cheminFile;
    }

    public void setCheminFile(String cheminFile) {
        this.cheminFile = cheminFile;
    }

    public String getEnCoursYN() {
        return enCoursYN;
    }

    public void setEnCoursYN(String enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InformationImageDTO)) {
            return false;
        }

        InformationImageDTO informationImageDTO = (InformationImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, informationImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformationImageDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", cheminPath='" + getCheminPath() + "'" +
            ", cheminFile='" + getCheminFile() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            "}";
    }
}
