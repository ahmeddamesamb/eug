package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.DisciplineSportiveEtudiant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisciplineSportiveEtudiantDTO implements Serializable {

    private Long id;

    private Boolean licenceSportiveYN;

    private Boolean competitionUGBYN;

    private DisciplineSportiveDTO disciplineSportive;

    private EtudiantDTO etudiant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLicenceSportiveYN() {
        return licenceSportiveYN;
    }

    public void setLicenceSportiveYN(Boolean licenceSportiveYN) {
        this.licenceSportiveYN = licenceSportiveYN;
    }

    public Boolean getCompetitionUGBYN() {
        return competitionUGBYN;
    }

    public void setCompetitionUGBYN(Boolean competitionUGBYN) {
        this.competitionUGBYN = competitionUGBYN;
    }

    public DisciplineSportiveDTO getDisciplineSportive() {
        return disciplineSportive;
    }

    public void setDisciplineSportive(DisciplineSportiveDTO disciplineSportive) {
        this.disciplineSportive = disciplineSportive;
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
        if (!(o instanceof DisciplineSportiveEtudiantDTO)) {
            return false;
        }

        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = (DisciplineSportiveEtudiantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, disciplineSportiveEtudiantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisciplineSportiveEtudiantDTO{" +
            "id=" + getId() +
            ", licenceSportiveYN='" + getLicenceSportiveYN() + "'" +
            ", competitionUGBYN='" + getCompetitionUGBYN() + "'" +
            ", disciplineSportive=" + getDisciplineSportive() +
            ", etudiant=" + getEtudiant() +
            "}";
    }
}
