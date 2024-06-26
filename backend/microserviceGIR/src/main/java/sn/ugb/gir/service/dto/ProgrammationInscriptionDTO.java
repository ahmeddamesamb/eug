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

    private LocalDate dateDebutProgrammation;

    private LocalDate dateFinProgrammation;

    private Boolean ouvertYN;

    private String emailUser;

    private LocalDate dateForclosClasse;

    private Boolean forclosClasseYN;

    private Boolean actifYN;

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

    public LocalDate getDateDebutProgrammation() {
        return dateDebutProgrammation;
    }

    public void setDateDebutProgrammation(LocalDate dateDebutProgrammation) {
        this.dateDebutProgrammation = dateDebutProgrammation;
    }

    public LocalDate getDateFinProgrammation() {
        return dateFinProgrammation;
    }

    public void setDateFinProgrammation(LocalDate dateFinProgrammation) {
        this.dateFinProgrammation = dateFinProgrammation;
    }

    public Boolean getOuvertYN() {
        return ouvertYN;
    }

    public void setOuvertYN(Boolean ouvertYN) {
        this.ouvertYN = ouvertYN;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public LocalDate getDateForclosClasse() {
        return dateForclosClasse;
    }

    public void setDateForclosClasse(LocalDate dateForclosClasse) {
        this.dateForclosClasse = dateForclosClasse;
    }

    public Boolean getForclosClasseYN() {
        return forclosClasseYN;
    }

    public void setForclosClasseYN(Boolean forclosClasseYN) {
        this.forclosClasseYN = forclosClasseYN;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
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
            ", dateDebutProgrammation='" + getDateDebutProgrammation() + "'" +
            ", dateFinProgrammation='" + getDateFinProgrammation() + "'" +
            ", ouvertYN='" + getOuvertYN() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            ", dateForclosClasse='" + getDateForclosClasse() + "'" +
            ", forclosClasseYN='" + getForclosClasseYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            ", anneeAcademique=" + getAnneeAcademique() +
            ", formation=" + getFormation() +
            ", campagne=" + getCampagne() +
            "}";
    }
}
