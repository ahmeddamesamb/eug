package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ministere.
 */
@Entity
@Table(name = "ministere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ministere")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ministere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_ministere", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomMinistere;

    @Column(name = "sigle_ministere")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sigleMinistere;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull
    @Column(name = "en_cours_yn", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean enCoursYN;

    @NotNull
    @Column(name = "actif_yn", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ministere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "ministere", "ufrs", "frais" }, allowSetters = true)
    private Set<Universite> universites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ministere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMinistere() {
        return this.nomMinistere;
    }

    public Ministere nomMinistere(String nomMinistere) {
        this.setNomMinistere(nomMinistere);
        return this;
    }

    public void setNomMinistere(String nomMinistere) {
        this.nomMinistere = nomMinistere;
    }

    public String getSigleMinistere() {
        return this.sigleMinistere;
    }

    public Ministere sigleMinistere(String sigleMinistere) {
        this.setSigleMinistere(sigleMinistere);
        return this;
    }

    public void setSigleMinistere(String sigleMinistere) {
        this.sigleMinistere = sigleMinistere;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Ministere dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Ministere dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getEnCoursYN() {
        return this.enCoursYN;
    }

    public Ministere enCoursYN(Boolean enCoursYN) {
        this.setEnCoursYN(enCoursYN);
        return this;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Ministere actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<Universite> getUniversites() {
        return this.universites;
    }

    public void setUniversites(Set<Universite> universites) {
        if (this.universites != null) {
            this.universites.forEach(i -> i.setMinistere(null));
        }
        if (universites != null) {
            universites.forEach(i -> i.setMinistere(this));
        }
        this.universites = universites;
    }

    public Ministere universites(Set<Universite> universites) {
        this.setUniversites(universites);
        return this;
    }

    public Ministere addUniversites(Universite universite) {
        this.universites.add(universite);
        universite.setMinistere(this);
        return this;
    }

    public Ministere removeUniversites(Universite universite) {
        this.universites.remove(universite);
        universite.setMinistere(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ministere)) {
            return false;
        }
        return getId() != null && getId().equals(((Ministere) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ministere{" +
            "id=" + getId() +
            ", nomMinistere='" + getNomMinistere() + "'" +
            ", sigleMinistere='" + getSigleMinistere() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
