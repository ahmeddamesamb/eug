package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Formation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationDTO implements Serializable {

    private Long id;

    private Integer nombreMensualites;

    private Integer fraisDossierYN;

    private Integer classeDiplomanteYN;

    private String libelleDiplome;

    private String codeFormation;

    private Integer nbreCreditsMin;

    private Integer estParcoursYN;

    private Integer lmdYN;

    private SpecialiteDTO specialite;

    private NiveauDTO niveau;

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

    public Integer getFraisDossierYN() {
        return fraisDossierYN;
    }

    public void setFraisDossierYN(Integer fraisDossierYN) {
        this.fraisDossierYN = fraisDossierYN;
    }

    public Integer getClasseDiplomanteYN() {
        return classeDiplomanteYN;
    }

    public void setClasseDiplomanteYN(Integer classeDiplomanteYN) {
        this.classeDiplomanteYN = classeDiplomanteYN;
    }

    public String getLibelleDiplome() {
        return libelleDiplome;
    }

    public void setLibelleDiplome(String libelleDiplome) {
        this.libelleDiplome = libelleDiplome;
    }

    public String getCodeFormation() {
        return codeFormation;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
    }

    public Integer getNbreCreditsMin() {
        return nbreCreditsMin;
    }

    public void setNbreCreditsMin(Integer nbreCreditsMin) {
        this.nbreCreditsMin = nbreCreditsMin;
    }

    public Integer getEstParcoursYN() {
        return estParcoursYN;
    }

    public void setEstParcoursYN(Integer estParcoursYN) {
        this.estParcoursYN = estParcoursYN;
    }

    public Integer getLmdYN() {
        return lmdYN;
    }

    public void setLmdYN(Integer lmdYN) {
        this.lmdYN = lmdYN;
    }

    public SpecialiteDTO getSpecialite() {
        return specialite;
    }

    public void setSpecialite(SpecialiteDTO specialite) {
        this.specialite = specialite;
    }

    public NiveauDTO getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauDTO niveau) {
        this.niveau = niveau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationDTO)) {
            return false;
        }

        FormationDTO formationDTO = (FormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationDTO{" +
            "id=" + getId() +
            ", nombreMensualites=" + getNombreMensualites() +
            ", fraisDossierYN=" + getFraisDossierYN() +
            ", classeDiplomanteYN=" + getClasseDiplomanteYN() +
            ", libelleDiplome='" + getLibelleDiplome() + "'" +
            ", codeFormation='" + getCodeFormation() + "'" +
            ", nbreCreditsMin=" + getNbreCreditsMin() +
            ", estParcoursYN=" + getEstParcoursYN() +
            ", lmdYN=" + getLmdYN() +
            ", specialite=" + getSpecialite() +
            ", niveau=" + getNiveau() +
            "}";
    }
}
