package sn.ugb.aide.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ressource.
 */
@Entity
@Table(name = "ressource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ressource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_ressource")
    private String nomRessource;

    @Column(name = "description_ressource")
    private String descriptionRessource;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ressource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomRessource() {
        return this.nomRessource;
    }

    public Ressource nomRessource(String nomRessource) {
        this.setNomRessource(nomRessource);
        return this;
    }

    public void setNomRessource(String nomRessource) {
        this.nomRessource = nomRessource;
    }

    public String getDescriptionRessource() {
        return this.descriptionRessource;
    }

    public Ressource descriptionRessource(String descriptionRessource) {
        this.setDescriptionRessource(descriptionRessource);
        return this;
    }

    public void setDescriptionRessource(String descriptionRessource) {
        this.descriptionRessource = descriptionRessource;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ressource)) {
            return false;
        }
        return getId() != null && getId().equals(((Ressource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ressource{" +
            "id=" + getId() +
            ", nomRessource='" + getNomRessource() + "'" +
            ", descriptionRessource='" + getDescriptionRessource() + "'" +
            "}";
    }
}
