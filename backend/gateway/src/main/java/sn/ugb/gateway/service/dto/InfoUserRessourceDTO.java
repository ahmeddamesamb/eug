package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.InfoUserRessource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfoUserRessourceDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate dateAjout;

    @NotNull(message = "must not be null")
    private Boolean enCoursYN;

    private InfosUserDTO infosUser;

    private RessourceDTO ressource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Boolean getEnCoursYN() {
        return enCoursYN;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public InfosUserDTO getInfosUser() {
        return infosUser;
    }

    public void setInfosUser(InfosUserDTO infosUser) {
        this.infosUser = infosUser;
    }

    public RessourceDTO getRessource() {
        return ressource;
    }

    public void setRessource(RessourceDTO ressource) {
        this.ressource = ressource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoUserRessourceDTO)) {
            return false;
        }

        InfoUserRessourceDTO infoUserRessourceDTO = (InfoUserRessourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, infoUserRessourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoUserRessourceDTO{" +
            "id=" + getId() +
            ", dateAjout='" + getDateAjout() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            ", infosUser=" + getInfosUser() +
            ", ressource=" + getRessource() +
            "}";
    }
}
