package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.HistoriqueConnexion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueConnexionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate dateDebutConnexion;

    @NotNull(message = "must not be null")
    private LocalDate dateFinConnexion;

    private String adresseIp;

    @NotNull(message = "must not be null")
    private Boolean actifYN;

    private InfosUserDTO infoUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebutConnexion() {
        return dateDebutConnexion;
    }

    public void setDateDebutConnexion(LocalDate dateDebutConnexion) {
        this.dateDebutConnexion = dateDebutConnexion;
    }

    public LocalDate getDateFinConnexion() {
        return dateFinConnexion;
    }

    public void setDateFinConnexion(LocalDate dateFinConnexion) {
        this.dateFinConnexion = dateFinConnexion;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
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
        if (!(o instanceof HistoriqueConnexionDTO)) {
            return false;
        }

        HistoriqueConnexionDTO historiqueConnexionDTO = (HistoriqueConnexionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueConnexionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueConnexionDTO{" +
            "id=" + getId() +
            ", dateDebutConnexion='" + getDateDebutConnexion() + "'" +
            ", dateFinConnexion='" + getDateFinConnexion() + "'" +
            ", adresseIp='" + getAdresseIp() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", infoUser=" + getInfoUser() +
            "}";
    }
}
