package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Doctorat.
 */
@Entity
@Table(name = "doctorat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "doctorat")
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
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sujet;

    @Column(name = "annee_inscription_doctorat")
    private LocalDate anneeInscriptionDoctorat;

    @Column(name = "encadreur_id")
    private Long encadreurId;

    @Column(name = "laboratoir_id")
    private Long laboratoirId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctorat")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "doctorat", "inscriptionAdministrativeFormation" }, allowSetters = true)
    private Set<InscriptionDoctorat> inscriptionDoctorats = new HashSet<>();

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

    public Long getEncadreurId() {
        return this.encadreurId;
    }

    public Doctorat encadreurId(Long encadreurId) {
        this.setEncadreurId(encadreurId);
        return this;
    }

    public void setEncadreurId(Long encadreurId) {
        this.encadreurId = encadreurId;
    }

    public Long getLaboratoirId() {
        return this.laboratoirId;
    }

    public Doctorat laboratoirId(Long laboratoirId) {
        this.setLaboratoirId(laboratoirId);
        return this;
    }

    public void setLaboratoirId(Long laboratoirId) {
        this.laboratoirId = laboratoirId;
    }

    public Set<InscriptionDoctorat> getInscriptionDoctorats() {
        return this.inscriptionDoctorats;
    }

    public void setInscriptionDoctorats(Set<InscriptionDoctorat> inscriptionDoctorats) {
        if (this.inscriptionDoctorats != null) {
            this.inscriptionDoctorats.forEach(i -> i.setDoctorat(null));
        }
        if (inscriptionDoctorats != null) {
            inscriptionDoctorats.forEach(i -> i.setDoctorat(this));
        }
        this.inscriptionDoctorats = inscriptionDoctorats;
    }

    public Doctorat inscriptionDoctorats(Set<InscriptionDoctorat> inscriptionDoctorats) {
        this.setInscriptionDoctorats(inscriptionDoctorats);
        return this;
    }

    public Doctorat addInscriptionDoctorats(InscriptionDoctorat inscriptionDoctorat) {
        this.inscriptionDoctorats.add(inscriptionDoctorat);
        inscriptionDoctorat.setDoctorat(this);
        return this;
    }

    public Doctorat removeInscriptionDoctorats(InscriptionDoctorat inscriptionDoctorat) {
        this.inscriptionDoctorats.remove(inscriptionDoctorat);
        inscriptionDoctorat.setDoctorat(null);
        return this;
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
