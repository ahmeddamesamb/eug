package sn.ugb.gateway.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gateway.domain.BlocFonctionnel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlocFonctionnelDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String libelleBloc;

    @NotNull(message = "must not be null")
    private LocalDate dateAjoutBloc;

    @NotNull(message = "must not be null")
    private Boolean actifYN;

    private ServiceUserDTO service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleBloc() {
        return libelleBloc;
    }

    public void setLibelleBloc(String libelleBloc) {
        this.libelleBloc = libelleBloc;
    }

    public LocalDate getDateAjoutBloc() {
        return dateAjoutBloc;
    }

    public void setDateAjoutBloc(LocalDate dateAjoutBloc) {
        this.dateAjoutBloc = dateAjoutBloc;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public ServiceUserDTO getService() {
        return service;
    }

    public void setService(ServiceUserDTO service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlocFonctionnelDTO)) {
            return false;
        }

        BlocFonctionnelDTO blocFonctionnelDTO = (BlocFonctionnelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blocFonctionnelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlocFonctionnelDTO{" +
            "id=" + getId() +
            ", libelleBloc='" + getLibelleBloc() + "'" +
            ", dateAjoutBloc='" + getDateAjoutBloc() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", service=" + getService() +
            "}";
    }
}
