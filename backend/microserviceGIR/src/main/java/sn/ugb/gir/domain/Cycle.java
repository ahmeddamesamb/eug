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
 * A Cycle.
 */
@Entity
@Table(name = "cycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cycle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_cycle", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleCycle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeCycle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "typeCycle", "formations" }, allowSetters = true)
    private Set<Niveau> niveaux = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeCycle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "typeFrais", "typeCycle", "universites", "paiementFrais" }, allowSetters = true)
    private Set<Frais> frais = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleCycle() {
        return this.libelleCycle;
    }

    public Cycle libelleCycle(String libelleCycle) {
        this.setLibelleCycle(libelleCycle);
        return this;
    }

    public void setLibelleCycle(String libelleCycle) {
        this.libelleCycle = libelleCycle;
    }

    public Set<Niveau> getNiveaux() {
        return this.niveaux;
    }

    public void setNiveaux(Set<Niveau> niveaus) {
        if (this.niveaux != null) {
            this.niveaux.forEach(i -> i.setTypeCycle(null));
        }
        if (niveaus != null) {
            niveaus.forEach(i -> i.setTypeCycle(this));
        }
        this.niveaux = niveaus;
    }

    public Cycle niveaux(Set<Niveau> niveaus) {
        this.setNiveaux(niveaus);
        return this;
    }

    public Cycle addNiveaux(Niveau niveau) {
        this.niveaux.add(niveau);
        niveau.setTypeCycle(this);
        return this;
    }

    public Cycle removeNiveaux(Niveau niveau) {
        this.niveaux.remove(niveau);
        niveau.setTypeCycle(null);
        return this;
    }

    public Set<Frais> getFrais() {
        return this.frais;
    }

    public void setFrais(Set<Frais> frais) {
        if (this.frais != null) {
            this.frais.forEach(i -> i.setTypeCycle(null));
        }
        if (frais != null) {
            frais.forEach(i -> i.setTypeCycle(this));
        }
        this.frais = frais;
    }

    public Cycle frais(Set<Frais> frais) {
        this.setFrais(frais);
        return this;
    }

    public Cycle addFrais(Frais frais) {
        this.frais.add(frais);
        frais.setTypeCycle(this);
        return this;
    }

    public Cycle removeFrais(Frais frais) {
        this.frais.remove(frais);
        frais.setTypeCycle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cycle)) {
            return false;
        }
        return getId() != null && getId().equals(((Cycle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cycle{" +
            "id=" + getId() +
            ", libelleCycle='" + getLibelleCycle() + "'" +
            "}";
    }
}
