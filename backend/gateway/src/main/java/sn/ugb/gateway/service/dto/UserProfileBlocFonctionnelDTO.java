package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.UserProfileBlocFonctionnel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileBlocFonctionnelDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate date;

    @NotNull(message = "must not be null")
    private Boolean enCoursYN;

    private UserProfileDTO userProfil;

    private BlocFonctionnelDTO blocFonctionnel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getEnCoursYN() {
        return enCoursYN;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public UserProfileDTO getUserProfil() {
        return userProfil;
    }

    public void setUserProfil(UserProfileDTO userProfil) {
        this.userProfil = userProfil;
    }

    public BlocFonctionnelDTO getBlocFonctionnel() {
        return blocFonctionnel;
    }

    public void setBlocFonctionnel(BlocFonctionnelDTO blocFonctionnel) {
        this.blocFonctionnel = blocFonctionnel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileBlocFonctionnelDTO)) {
            return false;
        }

        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = (UserProfileBlocFonctionnelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileBlocFonctionnelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileBlocFonctionnelDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            ", userProfil=" + getUserProfil() +
            ", blocFonctionnel=" + getBlocFonctionnel() +
            "}";
    }
}
