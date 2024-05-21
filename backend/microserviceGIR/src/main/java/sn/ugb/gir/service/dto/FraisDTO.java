package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import sn.ugb.gir.domain.enumeration.CYCLE;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Frais} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FraisDTO implements Serializable {

    private Long id;

    private Double valeurFrais;

    private String descriptionFrais;

    private Integer fraispourAssimileYN;

    private CYCLE cycle;

    private Double dia;

    private Double dip;

    private Float dipPrivee;

    private LocalDate dateApplication;

    private LocalDate dateFin;

    private Integer estEnApplicationYN;

    private TypeFraisDTO typeFrais;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValeurFrais() {
        return valeurFrais;
    }

    public void setValeurFrais(Double valeurFrais) {
        this.valeurFrais = valeurFrais;
    }

    public String getDescriptionFrais() {
        return descriptionFrais;
    }

    public void setDescriptionFrais(String descriptionFrais) {
        this.descriptionFrais = descriptionFrais;
    }

    public Integer getFraispourAssimileYN() {
        return fraispourAssimileYN;
    }

    public void setFraispourAssimileYN(Integer fraispourAssimileYN) {
        this.fraispourAssimileYN = fraispourAssimileYN;
    }

    public CYCLE getCycle() {
        return cycle;
    }

    public void setCycle(CYCLE cycle) {
        this.cycle = cycle;
    }

    public Double getDia() {
        return dia;
    }

    public void setDia(Double dia) {
        this.dia = dia;
    }

    public Double getDip() {
        return dip;
    }

    public void setDip(Double dip) {
        this.dip = dip;
    }

    public Float getDipPrivee() {
        return dipPrivee;
    }

    public void setDipPrivee(Float dipPrivee) {
        this.dipPrivee = dipPrivee;
    }

    public LocalDate getDateApplication() {
        return dateApplication;
    }

    public void setDateApplication(LocalDate dateApplication) {
        this.dateApplication = dateApplication;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getEstEnApplicationYN() {
        return estEnApplicationYN;
    }

    public void setEstEnApplicationYN(Integer estEnApplicationYN) {
        this.estEnApplicationYN = estEnApplicationYN;
    }

    public TypeFraisDTO getTypeFrais() {
        return typeFrais;
    }

    public void setTypeFrais(TypeFraisDTO typeFrais) {
        this.typeFrais = typeFrais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FraisDTO)) {
            return false;
        }

        FraisDTO fraisDTO = (FraisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fraisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraisDTO{" +
            "id=" + getId() +
            ", valeurFrais=" + getValeurFrais() +
            ", descriptionFrais='" + getDescriptionFrais() + "'" +
            ", fraispourAssimileYN=" + getFraispourAssimileYN() +
            ", cycle='" + getCycle() + "'" +
            ", dia=" + getDia() +
            ", dip=" + getDip() +
            ", dipPrivee=" + getDipPrivee() +
            ", dateApplication='" + getDateApplication() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", estEnApplicationYN=" + getEstEnApplicationYN() +
            ", typeFrais=" + getTypeFrais() +
            "}";
    }
}
