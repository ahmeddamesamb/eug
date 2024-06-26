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
 * A TypeAdmission.
 */
@Entity
@Table(name = "type_admission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "typeadmission")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeAdmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_type_admission", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleTypeAdmission;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeAdmission")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "typeAdmission", "anneeAcademique", "etudiant", "processusDinscriptionAdministrative", "inscriptionAdministrativeFormations",
        },
        allowSetters = true
    )
    private Set<InscriptionAdministrative> inscriptionAdministratives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeAdmission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeAdmission() {
        return this.libelleTypeAdmission;
    }

    public TypeAdmission libelleTypeAdmission(String libelleTypeAdmission) {
        this.setLibelleTypeAdmission(libelleTypeAdmission);
        return this;
    }

    public void setLibelleTypeAdmission(String libelleTypeAdmission) {
        this.libelleTypeAdmission = libelleTypeAdmission;
    }

    public Set<InscriptionAdministrative> getInscriptionAdministratives() {
        return this.inscriptionAdministratives;
    }

    public void setInscriptionAdministratives(Set<InscriptionAdministrative> inscriptionAdministratives) {
        if (this.inscriptionAdministratives != null) {
            this.inscriptionAdministratives.forEach(i -> i.setTypeAdmission(null));
        }
        if (inscriptionAdministratives != null) {
            inscriptionAdministratives.forEach(i -> i.setTypeAdmission(this));
        }
        this.inscriptionAdministratives = inscriptionAdministratives;
    }

    public TypeAdmission inscriptionAdministratives(Set<InscriptionAdministrative> inscriptionAdministratives) {
        this.setInscriptionAdministratives(inscriptionAdministratives);
        return this;
    }

    public TypeAdmission addInscriptionAdministratives(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministratives.add(inscriptionAdministrative);
        inscriptionAdministrative.setTypeAdmission(this);
        return this;
    }

    public TypeAdmission removeInscriptionAdministratives(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministratives.remove(inscriptionAdministrative);
        inscriptionAdministrative.setTypeAdmission(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeAdmission)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeAdmission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeAdmission{" +
            "id=" + getId() +
            ", libelleTypeAdmission='" + getLibelleTypeAdmission() + "'" +
            "}";
    }
}
