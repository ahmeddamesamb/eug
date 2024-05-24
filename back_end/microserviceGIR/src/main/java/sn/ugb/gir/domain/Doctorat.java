package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Doctorat.
 */
@Entity
@Table(name = "doctorat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Doctorat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sujet", nullable = false)
    private String sujet;

    @Column(name = "annee_inscription_doctorat")
    private LocalDate anneeInscriptionDoctorat;

    @Column(name = "encadreur_id")
    private Integer encadreurId;

    @Column(name = "laboratoir_id")
    private Integer laboratoirId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Doctorat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSujet() {
        return this.sujet;
    }

    public Doctorat sujet(String sujet) {
        this.setSujet(sujet);
        return this;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public LocalDate getAnneeInscriptionDoctorat() {
        return this.anneeInscriptionDoctorat;
    }

    public Doctorat anneeInscriptionDoctorat(LocalDate anneeInscriptionDoctorat) {
        this.setAnneeInscriptionDoctorat(anneeInscriptionDoctorat);
        return this;
    }

    public void setAnneeInscriptionDoctorat(LocalDate anneeInscriptionDoctorat) {
        this.anneeInscriptionDoctorat = anneeInscriptionDoctorat;
    }

    public Integer getEncadreurId() {
        return this.encadreurId;
    }

    public Doctorat encadreurId(Integer encadreurId) {
        this.setEncadreurId(encadreurId);
        return this;
    }

    public void setEncadreurId(Integer encadreurId) {
        this.encadreurId = encadreurId;
    }

    public Integer getLaboratoirId() {
        return this.laboratoirId;
    }

    public Doctorat laboratoirId(Integer laboratoirId) {
        this.setLaboratoirId(laboratoirId);
        return this;
    }

    public void setLaboratoirId(Integer laboratoirId) {
        this.laboratoirId = laboratoirId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doctorat)) {
            return false;
        }
        return getId() != null && getId().equals(((Doctorat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doctorat{" +
            "id=" + getId() +
            ", sujet='" + getSujet() + "'" +
            ", anneeInscriptionDoctorat='" + getAnneeInscriptionDoctorat() + "'" +
            ", encadreurId=" + getEncadreurId() +
            ", laboratoirId=" + getLaboratoirId() +
            "}";
    }
}
