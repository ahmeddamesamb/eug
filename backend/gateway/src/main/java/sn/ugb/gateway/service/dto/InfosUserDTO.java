package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.InfosUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfosUserDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate dateAjout;

    @NotNull(message = "must not be null")
    private Boolean actifYN;

    private UserDTO user;

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

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfosUserDTO)) {
            return false;
        }

        InfosUserDTO infosUserDTO = (InfosUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, infosUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfosUserDTO{" +
            "id=" + getId() +
            ", dateAjout='" + getDateAjout() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
