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
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationInvalide implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "actif_yn")
    private Integer actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "niveau", "specialite", "formationAutorisees", "formationPrivee" }, allowSetters = true)
    private Formation formation;

    @ManyToOne(fetch = FetchType.LAZY)
    private AnneeAcademique anneAcademique;

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

    public Integer getActifYN() {
        return this.actifYN;
    }

    public FormationInvalide actifYN(Integer actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Integer actifYN) {
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

    public AnneeAcademique getAnneAcademique() {
        return this.anneAcademique;
    }

    public void setAnneAcademique(AnneeAcademique anneeAcademique) {
        this.anneAcademique = anneeAcademique;
    }

    public FormationInvalide anneAcademique(AnneeAcademique anneeAcademique) {
        this.setAnneAcademique(anneeAcademique);
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
            ", actifYN=" + getActifYN() +
            "}";
    }
}
