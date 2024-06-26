package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FormationInvalide.
 */
@Entity
@Table(name = "formation_invalide")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formationinvalide")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationInvalide implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "typeFormation",
            "niveau",
            "specialite",
            "departement",
            "formationPrivee",
            "formationInvalides",
            "inscriptionAdministrativeFormations",
            "programmationInscriptions",
            "formationAutorisees",
        },
        allowSetters = true
    )
    private Formation formation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inscriptionAdministratives", "formationInvalides", "programmationInscriptions" }, allowSetters = true)
    private AnneeAcademique anneeAcademique;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormationInvalide id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public FormationInvalide actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public FormationInvalide formation(Formation formation) {
        this.setFormation(formation);
        return this;
    }

    public AnneeAcademique getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public FormationInvalide anneeAcademique(AnneeAcademique anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationInvalide)) {
            return false;
        }
        return getId() != null && getId().equals(((FormationInvalide) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationInvalide{" +
            "id=" + getId() +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
