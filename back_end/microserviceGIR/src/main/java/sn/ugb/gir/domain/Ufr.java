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
 * A Ufr.
 */
@Entity
@Table(name = "ufr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ufr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libele_ufr", nullable = false, unique = true)
    private String libeleUfr;

    @NotNull
    @Column(name = "sigle_ufr", nullable = false, unique = true)
    private String sigleUfr;

    @NotNull
    @Column(name = "systeme_lmdyn", nullable = false)
    private Integer systemeLMDYN;

    @Column(name = "ordre_stat")
    private Integer ordreStat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ministere" }, allowSetters = true)
    private Universite universite;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "ufrs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ufrs" }, allowSetters = true)
    private Set<Domaine> domaines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ufr id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleUfr() {
        return this.libeleUfr;
    }

    public Ufr libeleUfr(String libeleUfr) {
        this.setLibeleUfr(libeleUfr);
        return this;
    }

    public void setLibeleUfr(String libeleUfr) {
        this.libeleUfr = libeleUfr;
    }

    public String getSigleUfr() {
        return this.sigleUfr;
    }

    public Ufr sigleUfr(String sigleUfr) {
        this.setSigleUfr(sigleUfr);
        return this;
    }

    public void setSigleUfr(String sigleUfr) {
        this.sigleUfr = sigleUfr;
    }

    public Integer getSystemeLMDYN() {
        return this.systemeLMDYN;
    }

    public Ufr systemeLMDYN(Integer systemeLMDYN) {
        this.setSystemeLMDYN(systemeLMDYN);
        return this;
    }

    public void setSystemeLMDYN(Integer systemeLMDYN) {
        this.systemeLMDYN = systemeLMDYN;
    }

    public Integer getOrdreStat() {
        return this.ordreStat;
    }

    public Ufr ordreStat(Integer ordreStat) {
        this.setOrdreStat(ordreStat);
        return this;
    }

    public void setOrdreStat(Integer ordreStat) {
        this.ordreStat = ordreStat;
    }

    public Universite getUniversite() {
        return this.universite;
    }

    public void setUniversite(Universite universite) {
        this.universite = universite;
    }

    public Ufr universite(Universite universite) {
        this.setUniversite(universite);
        return this;
    }

    public Set<Domaine> getDomaines() {
        return this.domaines;
    }

    public void setDomaines(Set<Domaine> domaines) {
        if (this.domaines != null) {
            this.domaines.forEach(i -> i.removeUfr(this));
        }
        if (domaines != null) {
            domaines.forEach(i -> i.addUfr(this));
        }
        this.domaines = domaines;
    }

    public Ufr domaines(Set<Domaine> domaines) {
        this.setDomaines(domaines);
        return this;
    }

    public Ufr addDomaine(Domaine domaine) {
        this.domaines.add(domaine);
        domaine.getUfrs().add(this);
        return this;
    }

    public Ufr removeDomaine(Domaine domaine) {
        this.domaines.remove(domaine);
        domaine.getUfrs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ufr)) {
            return false;
        }
        return getId() != null && getId().equals(((Ufr) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ufr{" +
            "id=" + getId() +
            ", libeleUfr='" + getLibeleUfr() + "'" +
            ", sigleUfr='" + getSigleUfr() + "'" +
            ", systemeLMDYN=" + getSystemeLMDYN() +
            ", ordreStat=" + getOrdreStat() +
            "}";
    }
}
