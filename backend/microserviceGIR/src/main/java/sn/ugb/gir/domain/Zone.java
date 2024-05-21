package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Zone.
 */
@Entity
@Table(name = "zone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Zone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_zone")
    private String libelleZone;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "zones")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private Set<Pays> pays = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Zone id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleZone() {
        return this.libelleZone;
    }

    public Zone libelleZone(String libelleZone) {
        this.setLibelleZone(libelleZone);
        return this;
    }

    public void setLibelleZone(String libelleZone) {
        this.libelleZone = libelleZone;
    }

    public Set<Pays> getPays() {
        return this.pays;
    }

    public void setPays(Set<Pays> pays) {
        if (this.pays != null) {
            this.pays.forEach(i -> i.removeZone(this));
        }
        if (pays != null) {
            pays.forEach(i -> i.addZone(this));
        }
        this.pays = pays;
    }

    public Zone pays(Set<Pays> pays) {
        this.setPays(pays);
        return this;
    }

    public Zone addPays(Pays pays) {
        this.pays.add(pays);
        pays.getZones().add(this);
        return this;
    }

    public Zone removePays(Pays pays) {
        this.pays.remove(pays);
        pays.getZones().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Zone)) {
            return false;
        }
        return getId() != null && getId().equals(((Zone) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Zone{" +
            "id=" + getId() +
            ", libelleZone='" + getLibelleZone() + "'" +
            "}";
    }
}
