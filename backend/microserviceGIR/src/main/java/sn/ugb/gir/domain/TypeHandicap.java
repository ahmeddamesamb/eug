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
 * A TypeHandicap.
 */
@Entity
@Table(name = "type_handicap")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "typehandicap")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeHandicap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_type_handicap", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleTypeHandicap;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeHandicap")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "etudiant", "typeHandicap", "typeBourse" }, allowSetters = true)
    private Set<InformationPersonnelle> informationPersonnelles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeHandicap id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeHandicap() {
        return this.libelleTypeHandicap;
    }

    public TypeHandicap libelleTypeHandicap(String libelleTypeHandicap) {
        this.setLibelleTypeHandicap(libelleTypeHandicap);
        return this;
    }

    public void setLibelleTypeHandicap(String libelleTypeHandicap) {
        this.libelleTypeHandicap = libelleTypeHandicap;
    }

    public Set<InformationPersonnelle> getInformationPersonnelles() {
        return this.informationPersonnelles;
    }

    public void setInformationPersonnelles(Set<InformationPersonnelle> informationPersonnelles) {
        if (this.informationPersonnelles != null) {
            this.informationPersonnelles.forEach(i -> i.setTypeHandicap(null));
        }
        if (informationPersonnelles != null) {
            informationPersonnelles.forEach(i -> i.setTypeHandicap(this));
        }
        this.informationPersonnelles = informationPersonnelles;
    }

    public TypeHandicap informationPersonnelles(Set<InformationPersonnelle> informationPersonnelles) {
        this.setInformationPersonnelles(informationPersonnelles);
        return this;
    }

    public TypeHandicap addInformationPersonnelles(InformationPersonnelle informationPersonnelle) {
        this.informationPersonnelles.add(informationPersonnelle);
        informationPersonnelle.setTypeHandicap(this);
        return this;
    }

    public TypeHandicap removeInformationPersonnelles(InformationPersonnelle informationPersonnelle) {
        this.informationPersonnelles.remove(informationPersonnelle);
        informationPersonnelle.setTypeHandicap(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeHandicap)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeHandicap) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeHandicap{" +
            "id=" + getId() +
            ", libelleTypeHandicap='" + getLibelleTypeHandicap() + "'" +
            "}";
    }
}
