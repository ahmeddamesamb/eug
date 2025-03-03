package sn.ugb.gd.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeRapport.
 */
@Entity
@Table(name = "type_rapport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeRapport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_type_rapport")
    private String libelleTypeRapport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeRapport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeRapport() {
        return this.libelleTypeRapport;
    }

    public TypeRapport libelleTypeRapport(String libelleTypeRapport) {
        this.setLibelleTypeRapport(libelleTypeRapport);
        return this;
    }

    public void setLibelleTypeRapport(String libelleTypeRapport) {
        this.libelleTypeRapport = libelleTypeRapport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeRapport)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeRapport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeRapport{" +
            "id=" + getId() +
            ", libelleTypeRapport='" + getLibelleTypeRapport() + "'" +
            "}";
    }
}
