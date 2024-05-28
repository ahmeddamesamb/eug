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


    private Integer nombreMensualites;

    private Integer paiementPremierMoisYN;

    private Integer paiementDernierMoisYN;

    private Integer fraisDossierYN;

    private Float coutTotal;

    private Float mensualite;

    @NotNull
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

    public Integer getPaiementPremierMoisYN() {
        return paiementPremierMoisYN;
    }

    public void setPaiementPremierMoisYN(Integer paiementPremierMoisYN) {
        this.paiementPremierMoisYN = paiementPremierMoisYN;
    }

    public Integer getPaiementDernierMoisYN() {
        return paiementDernierMoisYN;
    }

    public void setPaiementDernierMoisYN(Integer paiementDernierMoisYN) {
        this.paiementDernierMoisYN = paiementDernierMoisYN;
    }

    public Integer getFraisDossierYN() {
        return fraisDossierYN;
    }

    public void setFraisDossierYN(Integer fraisDossierYN) {
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
            ", paiementPremierMoisYN=" + getPaiementPremierMoisYN() +
            ", paiementDernierMoisYN=" + getPaiementDernierMoisYN() +
            ", fraisDossierYN=" + getFraisDossierYN() +
            ", coutTotal=" + getCoutTotal() +
            ", mensualite=" + getMensualite() +
            ", formation=" + getFormation() +
            "}";
    }
}
