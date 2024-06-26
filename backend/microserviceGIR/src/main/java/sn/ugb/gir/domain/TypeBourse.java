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
 * A TypeBourse.
 */
@Entity
@Table(name = "type_bourse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "typebourse")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeBourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_type_bourse", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleTypeBourse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeBourse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "etudiant", "typeHandicap", "typeBourse" }, allowSetters = true)
    private Set<InformationPersonnelle> informationPersonnelles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeBourse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeBourse() {
        return this.libelleTypeBourse;
    }

    public TypeBourse libelleTypeBourse(String libelleTypeBourse) {
        this.setLibelleTypeBourse(libelleTypeBourse);
        return this;
    }

    public void setLibelleTypeBourse(String libelleTypeBourse) {
        this.libelleTypeBourse = libelleTypeBourse;
    }

    public Set<InformationPersonnelle> getInformationPersonnelles() {
        return this.informationPersonnelles;
    }

    public void setInformationPersonnelles(Set<InformationPersonnelle> informationPersonnelles) {
        if (this.informationPersonnelles != null) {
            this.informationPersonnelles.forEach(i -> i.setTypeBourse(null));
        }
        if (informationPersonnelles != null) {
            informationPersonnelles.forEach(i -> i.setTypeBourse(this));
        }
        this.informationPersonnelles = informationPersonnelles;
    }

    public TypeBourse informationPersonnelles(Set<InformationPersonnelle> informationPersonnelles) {
        this.setInformationPersonnelles(informationPersonnelles);
        return this;
    }

    public TypeBourse addInformationPersonnelles(InformationPersonnelle informationPersonnelle) {
        this.informationPersonnelles.add(informationPersonnelle);
        informationPersonnelle.setTypeBourse(this);
        return this;
    }

    public TypeBourse removeInformationPersonnelles(InformationPersonnelle informationPersonnelle) {
        this.informationPersonnelles.remove(informationPersonnelle);
        informationPersonnelle.setTypeBourse(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeBourse)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeBourse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeBourse{" +
            "id=" + getId() +
            ", libelleTypeBourse='" + getLibelleTypeBourse() + "'" +
            "}";
    }
}
