package sn.ugb.aua.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Laboratoire.
 */
@Entity
@Table(name = "laboratoire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Laboratoire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_laboratoire")
    private String nomLaboratoire;

    @Column(name = "laboratoire_cotutelle_yn")
    private Integer laboratoireCotutelleYN;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Laboratoire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLaboratoire() {
        return this.nomLaboratoire;
    }

    public Laboratoire nomLaboratoire(String nomLaboratoire) {
        this.setNomLaboratoire(nomLaboratoire);
        return this;
    }

    public void setNomLaboratoire(String nomLaboratoire) {
        this.nomLaboratoire = nomLaboratoire;
    }

    public Integer getLaboratoireCotutelleYN() {
        return this.laboratoireCotutelleYN;
    }

    public Laboratoire laboratoireCotutelleYN(Integer laboratoireCotutelleYN) {
        this.setLaboratoireCotutelleYN(laboratoireCotutelleYN);
        return this;
    }

    public void setLaboratoireCotutelleYN(Integer laboratoireCotutelleYN) {
        this.laboratoireCotutelleYN = laboratoireCotutelleYN;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Laboratoire)) {
            return false;
        }
        return getId() != null && getId().equals(((Laboratoire) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Laboratoire{" +
            "id=" + getId() +
            ", nomLaboratoire='" + getNomLaboratoire() + "'" +
            ", laboratoireCotutelleYN=" + getLaboratoireCotutelleYN() +
            "}";
    }
}
