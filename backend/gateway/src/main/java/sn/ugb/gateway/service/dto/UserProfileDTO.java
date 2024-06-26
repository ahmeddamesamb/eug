package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.UserProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate dateDebut;

    @NotNull(message = "must not be null")
    private LocalDate dateFin;

    @NotNull(message = "must not be null")
    private Boolean enCoursYN;

    private ProfilDTO profil;

    private InfosUserDTO infoUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getEnCoursYN() {
        return enCoursYN;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public ProfilDTO getProfil() {
        return profil;
    }

    public void setProfil(ProfilDTO profil) {
        this.profil = profil;
    }

    public InfosUserDTO getInfoUser() {
        return infoUser;
    }

    public void setInfoUser(InfosUserDTO infoUser) {
        this.infoUser = infoUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            ", profil=" + getProfil() +
            ", infoUser=" + getInfoUser() +
            "}";
    }
}
