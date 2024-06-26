package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InscriptionDoctorat.
 */
@Entity
@Table(name = "inscription_doctorat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "inscriptiondoctorat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionDoctorat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "source_financement")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sourceFinancement;

    @Column(name = "co_encadreur_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String coEncadreurId;

    @Column(name = "nombre_inscription")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nombreInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptionDoctorats" }, allowSetters = true)
    private Doctorat doctorat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "inscriptionAdministrative", "formation", "inscriptionDoctorats", "paiementFrais", "paiementFormationPrivees" },
        allowSetters = true
    )
    private InscriptionAdministrativeFormation inscriptionAdministrativeFormation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InscriptionDoctorat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceFinancement() {
        return this.sourceFinancement;
    }

    public InscriptionDoctorat sourceFinancement(String sourceFinancement) {
        this.setSourceFinancement(sourceFinancement);
        return this;
    }

    public void setSourceFinancement(String sourceFinancement) {
        this.sourceFinancement = sourceFinancement;
    }

    public String getCoEncadreurId() {
        return this.coEncadreurId;
    }

    public InscriptionDoctorat coEncadreurId(String coEncadreurId) {
        this.setCoEncadreurId(coEncadreurId);
        return this;
    }

    public void setCoEncadreurId(String coEncadreurId) {
        this.coEncadreurId = coEncadreurId;
    }

    public Integer getNombreInscription() {
        return this.nombreInscription;
    }

    public InscriptionDoctorat nombreInscription(Integer nombreInscription) {
        this.setNombreInscription(nombreInscription);
        return this;
    }

    public void setNombreInscription(Integer nombreInscription) {
        this.nombreInscription = nombreInscription;
    }

    public Doctorat getDoctorat() {
        return this.doctorat;
    }

    public void setDoctorat(Doctorat doctorat) {
        this.doctorat = doctorat;
    }

    public InscriptionDoctorat doctorat(Doctorat doctorat) {
        this.setDoctorat(doctorat);
        return this;
    }

    public InscriptionAdministrativeFormation getInscriptionAdministrativeFormation() {
        return this.inscriptionAdministrativeFormation;
    }

    public void setInscriptionAdministrativeFormation(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    }

    public InscriptionDoctorat inscriptionAdministrativeFormation(InscriptionAdministrativeFormation inscriptionAdministrativeFormation) {
        this.setInscriptionAdministrativeFormation(inscriptionAdministrativeFormation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionDoctorat)) {
            return false;
        }
        return getId() != null && getId().equals(((InscriptionDoctorat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionDoctorat{" +
            "id=" + getId() +
            ", sourceFinancement='" + getSourceFinancement() + "'" +
            ", coEncadreurId='" + getCoEncadreurId() + "'" +
            ", nombreInscription=" + getNombreInscription() +
            "}";
    }
}
