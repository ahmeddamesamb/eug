package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Ministere} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MinistereDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomMinistere;

    private String sigleMinistere;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Boolean enCoursYN;

    private Boolean actifYN;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMinistere() {
        return nomMinistere;
    }

    public void setNomMinistere(String nomMinistere) {
        this.nomMinistere = nomMinistere;
    }

    public String getSigleMinistere() {
        return sigleMinistere;
    }

    public void setSigleMinistere(String sigleMinistere) {
        this.sigleMinistere = sigleMinistere;
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

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MinistereDTO)) {
            return false;
        }

        MinistereDTO ministereDTO = (MinistereDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ministereDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MinistereDTO{" +
            "id=" + getId() +
            ", nomMinistere='" + getNomMinistere() + "'" +
            ", sigleMinistere='" + getSigleMinistere() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
