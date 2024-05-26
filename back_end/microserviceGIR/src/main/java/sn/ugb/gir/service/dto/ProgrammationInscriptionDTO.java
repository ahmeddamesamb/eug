package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.ProgrammationInscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgrammationInscriptionDTO implements Serializable {

    private Long id;

    private String libelleProgrammation;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Integer ouvertYN;

    private String emailUser;

    private AnneeAcademiqueDTO anneeAcademique;

    private FormationDTO formation;

    private CampagneDTO campagne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleProgrammation() {
        return libelleProgrammation;
    }

    public void setLibelleProgrammation(String libelleProgrammation) {
        this.libelleProgrammation = libelleProgrammation;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getOuvertYN() {
        return ouvertYN;
    }

    public void setOuvertYN(Integer ouvertYN) {
        this.ouvertYN = ouvertYN;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public AnneeAcademiqueDTO getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademiqueDTO anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    public CampagneDTO getCampagne() {
        return campagne;
    }

    public void setCampagne(CampagneDTO campagne) {
        this.campagne = campagne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgrammationInscriptionDTO)) {
            return false;
        }

        ProgrammationInscriptionDTO programmationInscriptionDTO = (ProgrammationInscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, programmationInscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgrammationInscriptionDTO{" +
            "id=" + getId() +
            ", libelleProgrammation='" + getLibelleProgrammation() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", ouvertYN=" + getOuvertYN() +
            ", emailUser='" + getEmailUser() + "'" +
            ", anneeAcademique=" + getAnneeAcademique() +
            ", formation=" + getFormation() +
            ", campagne=" + getCampagne() +
            "}";
    }
}
