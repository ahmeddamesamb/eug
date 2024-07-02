package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Frais} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FraisDTO implements Serializable {

    private Long id;

    @NotNull
    private Double valeurFrais;
    @NotNull
    private String descriptionFrais;
    @NotNull
    private Boolean fraisPourAssimileYN;

    private Boolean fraisPourExonererYN;

    private Double dia;

    private Double dip;

    private Double fraisPrivee;

    @NotNull
    private LocalDate dateApplication;

    private LocalDate dateFin;

    @NotNull
    private Boolean estEnApplicationYN;

    private Boolean actifYN;
    @NotNull
    private TypeFraisDTO typeFrais;
    @NotNull
    private CycleDTO typeCycle;

    private Set<UniversiteDTO> universites = new HashSet<>();

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

    public Boolean getFraisPourAssimileYN() {
        return fraisPourAssimileYN;
    }

    public void setFraisPourAssimileYN(Boolean fraisPourAssimileYN) {
        this.fraisPourAssimileYN = fraisPourAssimileYN;
    }

    public Boolean getFraisPourExonererYN() {
        return fraisPourExonererYN;
    }

    public void setFraisPourExonererYN(Boolean fraisPourExonererYN) {
        this.fraisPourExonererYN = fraisPourExonererYN;
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

    public Double getFraisPrivee() {
        return fraisPrivee;
    }

    public void setFraisPrivee(Double fraisPrivee) {
        this.fraisPrivee = fraisPrivee;
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

    public Boolean getEstEnApplicationYN() {
        return estEnApplicationYN;
    }

    public void setEstEnApplicationYN(Boolean estEnApplicationYN) {
        this.estEnApplicationYN = estEnApplicationYN;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public TypeFraisDTO getTypeFrais() {
        return typeFrais;
    }

    public void setTypeFrais(TypeFraisDTO typeFrais) {
        this.typeFrais = typeFrais;
    }

    public CycleDTO getTypeCycle() {
        return typeCycle;
    }

    public void setTypeCycle(CycleDTO typeCycle) {
        this.typeCycle = typeCycle;
    }

    public Set<UniversiteDTO> getUniversites() {
        return universites;
    }

    public void setUniversites(Set<UniversiteDTO> universites) {
        this.universites = universites;
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
            ", fraisPourAssimileYN='" + getFraisPourAssimileYN() + "'" +
            ", fraisPourExonererYN='" + getFraisPourExonererYN() + "'" +
            ", dia=" + getDia() +
            ", dip=" + getDip() +
            ", fraisPrivee=" + getFraisPrivee() +
            ", dateApplication='" + getDateApplication() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", estEnApplicationYN='" + getEstEnApplicationYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", typeFrais=" + getTypeFrais() +
            ", typeCycle=" + getTypeCycle() +
            ", universites=" + getUniversites() +
            "}";
    }
}
