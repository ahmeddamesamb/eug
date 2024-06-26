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
 * A Region.
 */
@Entity
@Table(name = "region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "region")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_region", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleRegion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "zones", "regions" }, allowSetters = true)
    private Pays pays;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "region",
            "typeSelection",
            "lycee",
            "informationPersonnelle",
            "baccalaureat",
            "disciplineSportiveEtudiants",
            "inscriptionAdministratives",
        },
        allowSetters = true
    )
    private Set<Etudiant> etudiants = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "region", "etudiants" }, allowSetters = true)
    private Set<Lycee> lycees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Region id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleRegion() {
        return this.libelleRegion;
    }

    public Region libelleRegion(String libelleRegion) {
        this.setLibelleRegion(libelleRegion);
        return this;
    }

    public void setLibelleRegion(String libelleRegion) {
        this.libelleRegion = libelleRegion;
    }

    public Pays getPays() {
        return this.pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Region pays(Pays pays) {
        this.setPays(pays);
        return this;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.setRegion(null));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.setRegion(this));
        }
        this.etudiants = etudiants;
    }

    public Region etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Region addEtudiants(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setRegion(this);
        return this;
    }

    public Region removeEtudiants(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setRegion(null);
        return this;
    }

    public Set<Lycee> getLycees() {
        return this.lycees;
    }

    public void setLycees(Set<Lycee> lycees) {
        if (this.lycees != null) {
            this.lycees.forEach(i -> i.setRegion(null));
        }
        if (lycees != null) {
            lycees.forEach(i -> i.setRegion(this));
        }
        this.lycees = lycees;
    }

    public Region lycees(Set<Lycee> lycees) {
        this.setLycees(lycees);
        return this;
    }

    public Region addLycees(Lycee lycee) {
        this.lycees.add(lycee);
        lycee.setRegion(this);
        return this;
    }

    public Region removeLycees(Lycee lycee) {
        this.lycees.remove(lycee);
        lycee.setRegion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return getId() != null && getId().equals(((Region) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Region{" +
            "id=" + getId() +
            ", libelleRegion='" + getLibelleRegion() + "'" +
            "}";
    }
}
