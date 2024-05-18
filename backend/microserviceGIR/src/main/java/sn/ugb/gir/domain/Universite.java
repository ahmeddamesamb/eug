package sn.ugb.gir.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Universite.
 */
@Entity
@Table(name = "universite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Universite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_universite")
    private String nomUniversite;

    @Column(name = "sigle_universite")
    private String sigleUniversite;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ministere ministere;

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
            "}";
    }
}
