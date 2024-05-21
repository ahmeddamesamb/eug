package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UFR.
 */
@Entity
@Table(name = "ufr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UFR implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libele_ufr")
    private String libeleUFR;

    @Column(name = "sigle_ufr")
    private String sigleUFR;

    @Column(name = "systeme_lmdyn")
    private Integer systemeLMDYN;

    @Column(name = "ordre_stat")
    private Integer ordreStat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ministere" }, allowSetters = true)
    private Universite universite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UFR id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibeleUFR() {
        return this.libeleUFR;
    }

    public UFR libeleUFR(String libeleUFR) {
        this.setLibeleUFR(libeleUFR);
        return this;
    }

    public void setLibeleUFR(String libeleUFR) {
        this.libeleUFR = libeleUFR;
    }

    public String getSigleUFR() {
        return this.sigleUFR;
    }

    public UFR sigleUFR(String sigleUFR) {
        this.setSigleUFR(sigleUFR);
        return this;
    }

    public void setSigleUFR(String sigleUFR) {
        this.sigleUFR = sigleUFR;
    }

    public Integer getSystemeLMDYN() {
        return this.systemeLMDYN;
    }

    public UFR systemeLMDYN(Integer systemeLMDYN) {
        this.setSystemeLMDYN(systemeLMDYN);
        return this;
    }

    public void setSystemeLMDYN(Integer systemeLMDYN) {
        this.systemeLMDYN = systemeLMDYN;
    }

    public Integer getOrdreStat() {
        return this.ordreStat;
    }

    public UFR ordreStat(Integer ordreStat) {
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

    public UFR universite(Universite universite) {
        this.setUniversite(universite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UFR)) {
            return false;
        }
        return getId() != null && getId().equals(((UFR) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UFR{" +
            "id=" + getId() +
            ", libeleUFR='" + getLibeleUFR() + "'" +
            ", sigleUFR='" + getSigleUFR() + "'" +
            ", systemeLMDYN=" + getSystemeLMDYN() +
            ", ordreStat=" + getOrdreStat() +
            "}";
    }
}
