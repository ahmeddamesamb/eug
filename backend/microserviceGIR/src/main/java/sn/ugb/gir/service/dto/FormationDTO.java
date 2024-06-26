package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Formation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationDTO implements Serializable {

    private Long id;

    private Boolean classeDiplomanteYN;

    private String libelleDiplome;

    private String codeFormation;

    private Integer nbreCreditsMin;

    private Boolean estParcoursYN;

    private Boolean lmdYN;

    private Boolean actifYN;

    private TypeFormationDTO typeFormation;

    private NiveauDTO niveau;

    private SpecialiteDTO specialite;

    private DepartementDTO departement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getClasseDiplomanteYN() {
        return classeDiplomanteYN;
    }

    public void setClasseDiplomanteYN(Boolean classeDiplomanteYN) {
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

    public Boolean getEstParcoursYN() {
        return estParcoursYN;
    }

    public void setEstParcoursYN(Boolean estParcoursYN) {
        this.estParcoursYN = estParcoursYN;
    }

    public Boolean getLmdYN() {
        return lmdYN;
    }

    public void setLmdYN(Boolean lmdYN) {
        this.lmdYN = lmdYN;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public TypeFormationDTO getTypeFormation() {
        return typeFormation;
    }

    public void setTypeFormation(TypeFormationDTO typeFormation) {
        this.typeFormation = typeFormation;
    }

    public NiveauDTO getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauDTO niveau) {
        this.niveau = niveau;
    }

    public SpecialiteDTO getSpecialite() {
        return specialite;
    }

    public void setSpecialite(SpecialiteDTO specialite) {
        this.specialite = specialite;
    }

    public DepartementDTO getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementDTO departement) {
        this.departement = departement;
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
            ", classeDiplomanteYN='" + getClasseDiplomanteYN() + "'" +
            ", libelleDiplome='" + getLibelleDiplome() + "'" +
            ", codeFormation='" + getCodeFormation() + "'" +
            ", nbreCreditsMin=" + getNbreCreditsMin() +
            ", estParcoursYN='" + getEstParcoursYN() + "'" +
            ", lmdYN='" + getLmdYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", typeFormation=" + getTypeFormation() +
            ", niveau=" + getNiveau() +
            ", specialite=" + getSpecialite() +
            ", departement=" + getDepartement() +
            "}";
    }
}
