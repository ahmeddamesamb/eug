package sn.ugb.aclc.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Candidat.
 */
@Entity
@Table(name = "candidat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Candidat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_canditat")
    private String nomCanditat;

    @Column(name = "prenom_candidat")
    private String prenomCandidat;

    @Column(name = "date_naissance_candidat")
    private LocalDate dateNaissanceCandidat;

    @Column(name = "email_candidat")
    private String emailCandidat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Candidat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCanditat() {
        return this.nomCanditat;
    }

    public Candidat nomCanditat(String nomCanditat) {
        this.setNomCanditat(nomCanditat);
        return this;
    }

    public void setNomCanditat(String nomCanditat) {
        this.nomCanditat = nomCanditat;
    }

    public String getPrenomCandidat() {
        return this.prenomCandidat;
    }

    public Candidat prenomCandidat(String prenomCandidat) {
        this.setPrenomCandidat(prenomCandidat);
        return this;
    }

    public void setPrenomCandidat(String prenomCandidat) {
        this.prenomCandidat = prenomCandidat;
    }

    public LocalDate getDateNaissanceCandidat() {
        return this.dateNaissanceCandidat;
    }

    public Candidat dateNaissanceCandidat(LocalDate dateNaissanceCandidat) {
        this.setDateNaissanceCandidat(dateNaissanceCandidat);
        return this;
    }

    public void setDateNaissanceCandidat(LocalDate dateNaissanceCandidat) {
        this.dateNaissanceCandidat = dateNaissanceCandidat;
    }

    public String getEmailCandidat() {
        return this.emailCandidat;
    }

    public Candidat emailCandidat(String emailCandidat) {
        this.setEmailCandidat(emailCandidat);
        return this;
    }

    public void setEmailCandidat(String emailCandidat) {
        this.emailCandidat = emailCandidat;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidat)) {
            return false;
        }
        return getId() != null && getId().equals(((Candidat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Candidat{" +
            "id=" + getId() +
            ", nomCanditat='" + getNomCanditat() + "'" +
            ", prenomCandidat='" + getPrenomCandidat() + "'" +
            ", dateNaissanceCandidat='" + getDateNaissanceCandidat() + "'" +
            ", emailCandidat='" + getEmailCandidat() + "'" +
            "}";
    }
}
