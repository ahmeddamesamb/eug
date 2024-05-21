package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Doctorat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctoratDTO implements Serializable {

    private Long id;

    private String sujet;

    private LocalDate anneeInscriptionDoctorat;

    private Integer encadreurId;

    private Integer laboratoirId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public LocalDate getAnneeInscriptionDoctorat() {
        return anneeInscriptionDoctorat;
    }

    public void setAnneeInscriptionDoctorat(LocalDate anneeInscriptionDoctorat) {
        this.anneeInscriptionDoctorat = anneeInscriptionDoctorat;
    }

    public Integer getEncadreurId() {
        return encadreurId;
    }

    public void setEncadreurId(Integer encadreurId) {
        this.encadreurId = encadreurId;
    }

    public Integer getLaboratoirId() {
        return laboratoirId;
    }

    public void setLaboratoirId(Integer laboratoirId) {
        this.laboratoirId = laboratoirId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctoratDTO)) {
            return false;
        }

        DoctoratDTO doctoratDTO = (DoctoratDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, doctoratDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctoratDTO{" +
            "id=" + getId() +
            ", sujet='" + getSujet() + "'" +
            ", anneeInscriptionDoctorat='" + getAnneeInscriptionDoctorat() + "'" +
            ", encadreurId=" + getEncadreurId() +
            ", laboratoirId=" + getLaboratoirId() +
            "}";
    }
}
