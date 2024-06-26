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
 * A Serie.
 */
@Entity
@Table(name = "serie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "serie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Serie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code_serie")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codeSerie;

    @NotNull
    @Column(name = "libelle_serie", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleSerie;

    @NotNull
    @Column(name = "sigle_serie", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sigleSerie;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "etudiant", "serie" }, allowSetters = true)
    private Set<Baccalaureat> baccalaureats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Serie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeSerie() {
        return this.codeSerie;
    }

    public Serie codeSerie(String codeSerie) {
        this.setCodeSerie(codeSerie);
        return this;
    }

    public void setCodeSerie(String codeSerie) {
        this.codeSerie = codeSerie;
    }

    public String getLibelleSerie() {
        return this.libelleSerie;
    }

    public Serie libelleSerie(String libelleSerie) {
        this.setLibelleSerie(libelleSerie);
        return this;
    }

    public void setLibelleSerie(String libelleSerie) {
        this.libelleSerie = libelleSerie;
    }

    public String getSigleSerie() {
        return this.sigleSerie;
    }

    public Serie sigleSerie(String sigleSerie) {
        this.setSigleSerie(sigleSerie);
        return this;
    }

    public void setSigleSerie(String sigleSerie) {
        this.sigleSerie = sigleSerie;
    }

    public Set<Baccalaureat> getBaccalaureats() {
        return this.baccalaureats;
    }

    public void setBaccalaureats(Set<Baccalaureat> baccalaureats) {
        if (this.baccalaureats != null) {
            this.baccalaureats.forEach(i -> i.setSerie(null));
        }
        if (baccalaureats != null) {
            baccalaureats.forEach(i -> i.setSerie(this));
        }
        this.baccalaureats = baccalaureats;
    }

    public Serie baccalaureats(Set<Baccalaureat> baccalaureats) {
        this.setBaccalaureats(baccalaureats);
        return this;
    }

    public Serie addBaccalaureats(Baccalaureat baccalaureat) {
        this.baccalaureats.add(baccalaureat);
        baccalaureat.setSerie(this);
        return this;
    }

    public Serie removeBaccalaureats(Baccalaureat baccalaureat) {
        this.baccalaureats.remove(baccalaureat);
        baccalaureat.setSerie(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Serie)) {
            return false;
        }
        return getId() != null && getId().equals(((Serie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Serie{" +
            "id=" + getId() +
            ", codeSerie='" + getCodeSerie() + "'" +
            ", libelleSerie='" + getLibelleSerie() + "'" +
            ", sigleSerie='" + getSigleSerie() + "'" +
            "}";
    }
}
