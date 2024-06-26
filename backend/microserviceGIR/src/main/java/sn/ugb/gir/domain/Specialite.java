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
 * A Specialite.
 */
@Entity
@Table(name = "specialite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "specialite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_specialites", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomSpecialites;

    @NotNull
    @Column(name = "sigle_specialites", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sigleSpecialites;

    @Column(name = "specialite_particulier_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean specialiteParticulierYN;

    @NotNull
    @Column(name = "specialites_payante_yn", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean specialitesPayanteYN;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "domaine", "specialites" }, allowSetters = true)
    private Mention mention;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "specialite")
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

    public Specialite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSpecialites() {
        return this.nomSpecialites;
    }

    public Specialite nomSpecialites(String nomSpecialites) {
        this.setNomSpecialites(nomSpecialites);
        return this;
    }

    public void setNomSpecialites(String nomSpecialites) {
        this.nomSpecialites = nomSpecialites;
    }

    public String getSigleSpecialites() {
        return this.sigleSpecialites;
    }

    public Specialite sigleSpecialites(String sigleSpecialites) {
        this.setSigleSpecialites(sigleSpecialites);
        return this;
    }

    public void setSigleSpecialites(String sigleSpecialites) {
        this.sigleSpecialites = sigleSpecialites;
    }

    public Boolean getSpecialiteParticulierYN() {
        return this.specialiteParticulierYN;
    }

    public Specialite specialiteParticulierYN(Boolean specialiteParticulierYN) {
        this.setSpecialiteParticulierYN(specialiteParticulierYN);
        return this;
    }

    public void setSpecialiteParticulierYN(Boolean specialiteParticulierYN) {
        this.specialiteParticulierYN = specialiteParticulierYN;
    }

    public Boolean getSpecialitesPayanteYN() {
        return this.specialitesPayanteYN;
    }

    public Specialite specialitesPayanteYN(Boolean specialitesPayanteYN) {
        this.setSpecialitesPayanteYN(specialitesPayanteYN);
        return this;
    }

    public void setSpecialitesPayanteYN(Boolean specialitesPayanteYN) {
        this.specialitesPayanteYN = specialitesPayanteYN;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Specialite actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Mention getMention() {
        return this.mention;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }

    public Specialite mention(Mention mention) {
        this.setMention(mention);
        return this;
    }

    public Set<Formation> getFormations() {
        return this.formations;
    }

    public void setFormations(Set<Formation> formations) {
        if (this.formations != null) {
            this.formations.forEach(i -> i.setSpecialite(null));
        }
        if (formations != null) {
            formations.forEach(i -> i.setSpecialite(this));
        }
        this.formations = formations;
    }

    public Specialite formations(Set<Formation> formations) {
        this.setFormations(formations);
        return this;
    }

    public Specialite addFormations(Formation formation) {
        this.formations.add(formation);
        formation.setSpecialite(this);
        return this;
    }

    public Specialite removeFormations(Formation formation) {
        this.formations.remove(formation);
        formation.setSpecialite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialite)) {
            return false;
        }
        return getId() != null && getId().equals(((Specialite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialite{" +
            "id=" + getId() +
            ", nomSpecialites='" + getNomSpecialites() + "'" +
            ", sigleSpecialites='" + getSigleSpecialites() + "'" +
            ", specialiteParticulierYN='" + getSpecialiteParticulierYN() + "'" +
            ", specialitesPayanteYN='" + getSpecialitesPayanteYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
