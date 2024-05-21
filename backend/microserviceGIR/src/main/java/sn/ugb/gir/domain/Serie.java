package sn.ugb.gir.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Serie.
 */
@Entity
@Table(name = "serie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Serie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code_serie")
    private String codeSerie;

    @Column(name = "libelle_serie")
    private String libelleSerie;

    @Column(name = "sigle_serie")
    private String sigleSerie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Serie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeSerie() {
        return this.codeSerie;
    }

    public Serie codeSerie(String codeSerie) {
        this.setCodeSerie(codeSerie);
        return this;
    }

    public void setCodeSerie(String codeSerie) {
        this.codeSerie = codeSerie;
    }

    public String getLibelleSerie() {
        return this.libelleSerie;
    }

    public Serie libelleSerie(String libelleSerie) {
        this.setLibelleSerie(libelleSerie);
        return this;
    }

    public void setLibelleSerie(String libelleSerie) {
        this.libelleSerie = libelleSerie;
    }

    public String getSigleSerie() {
        return this.sigleSerie;
    }

    public Serie sigleSerie(String sigleSerie) {
        this.setSigleSerie(sigleSerie);
        return this;
    }

    public void setSigleSerie(String sigleSerie) {
        this.sigleSerie = sigleSerie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Serie)) {
            return false;
        }
        return getId() != null && getId().equals(((Serie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Serie{" +
            "id=" + getId() +
            ", codeSerie='" + getCodeSerie() + "'" +
            ", libelleSerie='" + getLibelleSerie() + "'" +
            ", sigleSerie='" + getSigleSerie() + "'" +
            "}";
    }
}
