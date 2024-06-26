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
@org.springframework.data.elasticsearch.annotations.Document(indexName = "laboratoire")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Laboratoire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nom;

    @Column(name = "laboratoire_cotutelle_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean laboratoireCotutelleYN;

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

    public String getNom() {
        return this.nom;
    }

    public Laboratoire nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Boolean getLaboratoireCotutelleYN() {
        return this.laboratoireCotutelleYN;
    }

    public Laboratoire laboratoireCotutelleYN(Boolean laboratoireCotutelleYN) {
        this.setLaboratoireCotutelleYN(laboratoireCotutelleYN);
        return this;
    }

    public void setLaboratoireCotutelleYN(Boolean laboratoireCotutelleYN) {
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
            ", nom='" + getNom() + "'" +
            ", laboratoireCotutelleYN='" + getLaboratoireCotutelleYN() + "'" +
            "}";
    }
}
