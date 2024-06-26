package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.FormationPrivee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationPriveeDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer nombreMensualites;

    private Boolean paiementPremierMoisYN;

    private Boolean paiementDernierMoisYN;

    private Boolean fraisDossierYN;

    @NotNull
    private Float coutTotal;

    @NotNull
    private Float mensualite;

    private Boolean actifYN;

    private FormationDTO formation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNombreMensualites() {
        return nombreMensualites;
    }

    public void setNombreMensualites(Integer nombreMensualites) {
        this.nombreMensualites = nombreMensualites;
    }

    public Boolean getPaiementPremierMoisYN() {
        return paiementPremierMoisYN;
    }

    public void setPaiementPremierMoisYN(Boolean paiementPremierMoisYN) {
        this.paiementPremierMoisYN = paiementPremierMoisYN;
    }

    public Boolean getPaiementDernierMoisYN() {
        return paiementDernierMoisYN;
    }

    public void setPaiementDernierMoisYN(Boolean paiementDernierMoisYN) {
        this.paiementDernierMoisYN = paiementDernierMoisYN;
    }

    public Boolean getFraisDossierYN() {
        return fraisDossierYN;
    }

    public void setFraisDossierYN(Boolean fraisDossierYN) {
        this.fraisDossierYN = fraisDossierYN;
    }

    public Float getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(Float coutTotal) {
        this.coutTotal = coutTotal;
    }

    public Float getMensualite() {
        return mensualite;
    }

    public void setMensualite(Float mensualite) {
        this.mensualite = mensualite;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationPriveeDTO)) {
            return false;
        }

        FormationPriveeDTO formationPriveeDTO = (FormationPriveeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formationPriveeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationPriveeDTO{" +
            "id=" + getId() +
            ", nombreMensualites=" + getNombreMensualites() +
            ", paiementPremierMoisYN='" + getPaiementPremierMoisYN() + "'" +
            ", paiementDernierMoisYN='" + getPaiementDernierMoisYN() + "'" +
            ", fraisDossierYN='" + getFraisDossierYN() + "'" +
            ", coutTotal=" + getCoutTotal() +
            ", mensualite=" + getMensualite() +
            ", actifYN='" + getActifYN() + "'" +
            ", formation=" + getFormation() +
            "}";
    }
}
