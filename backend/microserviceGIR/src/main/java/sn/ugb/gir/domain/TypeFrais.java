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
 * A TypeFrais.
 */
@Entity
@Table(name = "type_frais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "typefrais")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeFrais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_type_frais", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleTypeFrais;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeFrais")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "typeFrais", "typeCycle", "universites", "paiementFrais" }, allowSetters = true)
    private Set<Frais> frais = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeFrais id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleTypeFrais() {
        return this.libelleTypeFrais;
    }

    public TypeFrais libelleTypeFrais(String libelleTypeFrais) {
        this.setLibelleTypeFrais(libelleTypeFrais);
        return this;
    }

    public void setLibelleTypeFrais(String libelleTypeFrais) {
        this.libelleTypeFrais = libelleTypeFrais;
    }

    public Set<Frais> getFrais() {
        return this.frais;
    }

    public void setFrais(Set<Frais> frais) {
        if (this.frais != null) {
            this.frais.forEach(i -> i.setTypeFrais(null));
        }
        if (frais != null) {
            frais.forEach(i -> i.setTypeFrais(this));
        }
        this.frais = frais;
    }

    public TypeFrais frais(Set<Frais> frais) {
        this.setFrais(frais);
        return this;
    }

    public TypeFrais addFrais(Frais frais) {
        this.frais.add(frais);
        frais.setTypeFrais(this);
        return this;
    }

    public TypeFrais removeFrais(Frais frais) {
        this.frais.remove(frais);
        frais.setTypeFrais(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeFrais)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeFrais) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeFrais{" +
            "id=" + getId() +
            ", libelleTypeFrais='" + getLibelleTypeFrais() + "'" +
            "}";
    }
}
