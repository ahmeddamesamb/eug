package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeFormation.
 */
@Entity
@Table(name = "type_formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "typeformation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_type_formation", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleTypeFormation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeFormation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
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
    private Set<Formation> formations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeFormation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeFormation() {
        return this.libelleTypeFormation;
    }

    public TypeFormation libelleTypeFormation(String libelleTypeFormation) {
        this.setLibelleTypeFormation(libelleTypeFormation);
        return this;
    }

    public void setLibelleTypeFormation(String libelleTypeFormation) {
        this.libelleTypeFormation = libelleTypeFormation;
    }

    public Set<Formation> getFormations() {
        return this.formations;
    }

    public void setFormations(Set<Formation> formations) {
        if (this.formations != null) {
            this.formations.forEach(i -> i.setTypeFormation(null));
        }
        if (formations != null) {
            formations.forEach(i -> i.setTypeFormation(this));
        }
        this.formations = formations;
    }

    public TypeFormation formations(Set<Formation> formations) {
        this.setFormations(formations);
        return this;
    }

    public TypeFormation addFormations(Formation formation) {
        this.formations.add(formation);
        formation.setTypeFormation(this);
        return this;
    }

    public TypeFormation removeFormations(Formation formation) {
        this.formations.remove(formation);
        formation.setTypeFormation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeFormation)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeFormation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeFormation{" +
            "id=" + getId() +
            ", libelleTypeFormation='" + getLibelleTypeFormation() + "'" +
            "}";
    }
}
