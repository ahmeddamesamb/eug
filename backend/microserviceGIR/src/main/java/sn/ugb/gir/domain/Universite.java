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
 * A Universite.
 */
@Entity
@Table(name = "universite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "universite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Universite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_universite", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomUniversite;

    @NotNull
    @Column(name = "sigle_universite", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sigleUniversite;

    @NotNull
    @Column(name = "actif_yn", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "universites" }, allowSetters = true)
    private Ministere ministere;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "universite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "universite", "domaines" }, allowSetters = true)
    private Set<Ufr> ufrs = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "universites")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "typeFrais", "typeCycle", "universites", "paiementFrais" }, allowSetters = true)
    private Set<Frais> frais = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Universite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUniversite() {
        return this.nomUniversite;
    }

    public Universite nomUniversite(String nomUniversite) {
        this.setNomUniversite(nomUniversite);
        return this;
    }

    public void setNomUniversite(String nomUniversite) {
        this.nomUniversite = nomUniversite;
    }

    public String getSigleUniversite() {
        return this.sigleUniversite;
    }

    public Universite sigleUniversite(String sigleUniversite) {
        this.setSigleUniversite(sigleUniversite);
        return this;
    }

    public void setSigleUniversite(String sigleUniversite) {
        this.sigleUniversite = sigleUniversite;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Universite actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Ministere getMinistere() {
        return this.ministere;
    }

    public void setMinistere(Ministere ministere) {
        this.ministere = ministere;
    }

    public Universite ministere(Ministere ministere) {
        this.setMinistere(ministere);
        return this;
    }

    public Set<Ufr> getUfrs() {
        return this.ufrs;
    }

    public void setUfrs(Set<Ufr> ufrs) {
        if (this.ufrs != null) {
            this.ufrs.forEach(i -> i.setUniversite(null));
        }
        if (ufrs != null) {
            ufrs.forEach(i -> i.setUniversite(this));
        }
        this.ufrs = ufrs;
    }

    public Universite ufrs(Set<Ufr> ufrs) {
        this.setUfrs(ufrs);
        return this;
    }

    public Universite addUfrs(Ufr ufr) {
        this.ufrs.add(ufr);
        ufr.setUniversite(this);
        return this;
    }

    public Universite removeUfrs(Ufr ufr) {
        this.ufrs.remove(ufr);
        ufr.setUniversite(null);
        return this;
    }

    public Set<Frais> getFrais() {
        return this.frais;
    }

    public void setFrais(Set<Frais> frais) {
        if (this.frais != null) {
            this.frais.forEach(i -> i.removeUniversite(this));
        }
        if (frais != null) {
            frais.forEach(i -> i.addUniversite(this));
        }
        this.frais = frais;
    }

    public Universite frais(Set<Frais> frais) {
        this.setFrais(frais);
        return this;
    }

    public Universite addFrais(Frais frais) {
        this.frais.add(frais);
        frais.getUniversites().add(this);
        return this;
    }

    public Universite removeFrais(Frais frais) {
        this.frais.remove(frais);
        frais.getUniversites().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Universite)) {
            return false;
        }
        return getId() != null && getId().equals(((Universite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Universite{" +
            "id=" + getId() +
            ", nomUniversite='" + getNomUniversite() + "'" +
            ", sigleUniversite='" + getSigleUniversite() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
