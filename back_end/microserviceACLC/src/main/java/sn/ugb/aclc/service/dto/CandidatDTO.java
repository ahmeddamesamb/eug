package sn.ugb.aclc.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.aclc.domain.Candidat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidatDTO implements Serializable {

    private Long id;

    private String nomCanditat;

    private String prenomCandidat;

    private LocalDate dateNaissanceCandidat;

    private String emailCandidat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCanditat() {
        return nomCanditat;
    }

    public void setNomCanditat(String nomCanditat) {
        this.nomCanditat = nomCanditat;
    }

    public String getPrenomCandidat() {
        return prenomCandidat;
    }

    public void setPrenomCandidat(String prenomCandidat) {
        this.prenomCandidat = prenomCandidat;
    }

    public LocalDate getDateNaissanceCandidat() {
        return dateNaissanceCandidat;
    }

    public void setDateNaissanceCandidat(LocalDate dateNaissanceCandidat) {
        this.dateNaissanceCandidat = dateNaissanceCandidat;
    }

    public String getEmailCandidat() {
        return emailCandidat;
    }

    public void setEmailCandidat(String emailCandidat) {
        this.emailCandidat = emailCandidat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidatDTO)) {
            return false;
        }

        CandidatDTO candidatDTO = (CandidatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidatDTO{" +
            "id=" + getId() +
            ", nomCanditat='" + getNomCanditat() + "'" +
            ", prenomCandidat='" + getPrenomCandidat() + "'" +
            ", dateNaissanceCandidat='" + getDateNaissanceCandidat() + "'" +
            ", emailCandidat='" + getEmailCandidat() + "'" +
            "}";
    }
}
