package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "departement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_depatement", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomDepatement;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "universite", "domaines" }, allowSetters = true)
    private Ufr ufr;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDepatement() {
        return this.nomDepatement;
    }

    public Departement nomDepatement(String nomDepatement) {
        this.setNomDepatement(nomDepatement);
        return this;
    }

    public void setNomDepatement(String nomDepatement) {
        this.nomDepatement = nomDepatement;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Departement actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Ufr getUfr() {
        return this.ufr;
    }

    public void setUfr(Ufr ufr) {
        this.ufr = ufr;
    }

    public Departement ufr(Ufr ufr) {
        this.setUfr(ufr);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return getId() != null && getId().equals(((Departement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", nomDepatement='" + getNomDepatement() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
