package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.InscriptionAdministrative} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionAdministrativeDTO implements Serializable {

    private Long id;

    private Integer nouveauInscritYN;

    private Integer repriseYN;

    private Integer autoriseYN;

    private Integer ordreInscription;

    private TypeAdmissionDTO typeAdmission;

    private AnneeAcademiqueDTO anneeAcademique;

    private EtudiantDTO etudiant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNouveauInscritYN() {
        return nouveauInscritYN;
    }

    public void setNouveauInscritYN(Integer nouveauInscritYN) {
        this.nouveauInscritYN = nouveauInscritYN;
    }

    public Integer getRepriseYN() {
        return repriseYN;
    }

    public void setRepriseYN(Integer repriseYN) {
        this.repriseYN = repriseYN;
    }

    public Integer getAutoriseYN() {
        return autoriseYN;
    }

    public void setAutoriseYN(Integer autoriseYN) {
        this.autoriseYN = autoriseYN;
    }

    public Integer getOrdreInscription() {
        return ordreInscription;
    }

    public void setOrdreInscription(Integer ordreInscription) {
        this.ordreInscription = ordreInscription;
    }

    public TypeAdmissionDTO getTypeAdmission() {
        return typeAdmission;
    }

    public void setTypeAdmission(TypeAdmissionDTO typeAdmission) {
        this.typeAdmission = typeAdmission;
    }

    public AnneeAcademiqueDTO getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademiqueDTO anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionAdministrativeDTO)) {
            return false;
        }

        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = (InscriptionAdministrativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inscriptionAdministrativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionAdministrativeDTO{" +
            "id=" + getId() +
            ", nouveauInscritYN=" + getNouveauInscritYN() +
            ", repriseYN=" + getRepriseYN() +
            ", autoriseYN=" + getAutoriseYN() +
            ", ordreInscription=" + getOrdreInscription() +
            ", typeAdmission=" + getTypeAdmission() +
            ", anneeAcademique=" + getAnneeAcademique() +
            ", etudiant=" + getEtudiant() +
            "}";
    }
}
