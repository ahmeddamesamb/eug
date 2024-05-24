package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.ugb.gir.domain.enumeration.Cycle;

/**
 * A Niveau.
 */
@Entity
@Table(name = "niveau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Niveau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_niveau", nullable = false, unique = true)
    private String libelleNiveau;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_niveau", nullable = false)
    private Cycle cycleNiveau;

    @NotNull
    @Column(name = "code_niveau", nullable = false)
    private String codeNiveau;

    @Column(name = "annee_etude")
    private String anneeEtude;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Niveau id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleNiveau() {
        return this.libelleNiveau;
    }

    public Niveau libelleNiveau(String libelleNiveau) {
        this.setLibelleNiveau(libelleNiveau);
        return this;
    }

    public void setLibelleNiveau(String libelleNiveau) {
        this.libelleNiveau = libelleNiveau;
    }

    public Cycle getCycleNiveau() {
        return this.cycleNiveau;
    }

    public Niveau cycleNiveau(Cycle cycleNiveau) {
        this.setCycleNiveau(cycleNiveau);
        return this;
    }

    public void setCycleNiveau(Cycle cycleNiveau) {
        this.cycleNiveau = cycleNiveau;
    }

    public String getCodeNiveau() {
        return this.codeNiveau;
    }

    public Niveau codeNiveau(String codeNiveau) {
        this.setCodeNiveau(codeNiveau);
        return this;
    }

    public void setCodeNiveau(String codeNiveau) {
        this.codeNiveau = codeNiveau;
    }

    public String getAnneeEtude() {
        return this.anneeEtude;
    }

    public Niveau anneeEtude(String anneeEtude) {
        this.setAnneeEtude(anneeEtude);
        return this;
    }

    public void setAnneeEtude(String anneeEtude) {
        this.anneeEtude = anneeEtude;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Niveau)) {
            return false;
        }
        return getId() != null && getId().equals(((Niveau) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Niveau{" +
            "id=" + getId() +
            ", libelleNiveau='" + getLibelleNiveau() + "'" +
            ", cycleNiveau='" + getCycleNiveau() + "'" +
            ", codeNiveau='" + getCodeNiveau() + "'" +
            ", anneeEtude='" + getAnneeEtude() + "'" +
            "}";
    }
}
