package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.InscriptionDoctorat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionDoctoratDTO implements Serializable {

    private Long id;

    private String sourceFinancement;

    private String coEncadreurId;

    private Integer nombreInscription;

    private DoctoratDTO doctorat;

    private InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceFinancement() {
        return sourceFinancement;
    }

    public void setSourceFinancement(String sourceFinancement) {
        this.sourceFinancement = sourceFinancement;
    }

    public String getCoEncadreurId() {
        return coEncadreurId;
    }

    public void setCoEncadreurId(String coEncadreurId) {
        this.coEncadreurId = coEncadreurId;
    }

    public Integer getNombreInscription() {
        return nombreInscription;
    }

    public void setNombreInscription(Integer nombreInscription) {
        this.nombreInscription = nombreInscription;
    }

    public DoctoratDTO getDoctorat() {
        return doctorat;
    }

    public void setDoctorat(DoctoratDTO doctorat) {
        this.doctorat = doctorat;
    }

    public InscriptionAdministrativeFormationDTO getInscriptionAdministrativeFormation() {
        return inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionDoctoratDTO)) {
            return false;
        }

        InscriptionDoctoratDTO inscriptionDoctoratDTO = (InscriptionDoctoratDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inscriptionDoctoratDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionDoctoratDTO{" +
            "id=" + getId() +
            ", sourceFinancement='" + getSourceFinancement() + "'" +
            ", coEncadreurId='" + getCoEncadreurId() + "'" +
            ", nombreInscription=" + getNombreInscription() +
            ", doctorat=" + getDoctorat() +
            ", inscriptionAdministrativeFormation=" + getInscriptionAdministrativeFormation() +
            "}";
    }
}
