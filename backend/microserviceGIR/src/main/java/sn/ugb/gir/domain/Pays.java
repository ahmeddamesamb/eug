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
 * A Pays.
 */
@Entity
@Table(name = "pays")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pays")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_pays", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libellePays;

    @Column(name = "pays_en_anglais")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paysEnAnglais;

    @Column(name = "nationalite")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nationalite;

    @Column(name = "code_pays")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codePays;

    @Column(name = "u_emoayn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean uEMOAYN;

    @Column(name = "c_edeaoyn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean cEDEAOYN;

    @Column(name = "r_imyn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean rIMYN;

    @Column(name = "autre_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean autreYN;

    @Column(name = "est_etranger_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean estEtrangerYN;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_pays__zone", joinColumns = @JoinColumn(name = "pays_id"), inverseJoinColumns = @JoinColumn(name = "zone_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays" }, allowSetters = true)
    private Set<Zone> zones = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "pays", "etudiants", "lycees" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pays id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibellePays() {
        return this.libellePays;
    }

    public Pays libellePays(String libellePays) {
        this.setLibellePays(libellePays);
        return this;
    }

    public void setLibellePays(String libellePays) {
        this.libellePays = libellePays;
    }

    public String getPaysEnAnglais() {
        return this.paysEnAnglais;
    }

    public Pays paysEnAnglais(String paysEnAnglais) {
        this.setPaysEnAnglais(paysEnAnglais);
        return this;
    }

    public void setPaysEnAnglais(String paysEnAnglais) {
        this.paysEnAnglais = paysEnAnglais;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public Pays nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getCodePays() {
        return this.codePays;
    }

    public Pays codePays(String codePays) {
        this.setCodePays(codePays);
        return this;
    }

    public void setCodePays(String codePays) {
        this.codePays = codePays;
    }

    public Boolean getuEMOAYN() {
        return this.uEMOAYN;
    }

    public Pays uEMOAYN(Boolean uEMOAYN) {
        this.setuEMOAYN(uEMOAYN);
        return this;
    }

    public void setuEMOAYN(Boolean uEMOAYN) {
        this.uEMOAYN = uEMOAYN;
    }

    public Boolean getcEDEAOYN() {
        return this.cEDEAOYN;
    }

    public Pays cEDEAOYN(Boolean cEDEAOYN) {
        this.setcEDEAOYN(cEDEAOYN);
        return this;
    }

    public void setcEDEAOYN(Boolean cEDEAOYN) {
        this.cEDEAOYN = cEDEAOYN;
    }

    public Boolean getrIMYN() {
        return this.rIMYN;
    }

    public Pays rIMYN(Boolean rIMYN) {
        this.setrIMYN(rIMYN);
        return this;
    }

    public void setrIMYN(Boolean rIMYN) {
        this.rIMYN = rIMYN;
    }

    public Boolean getAutreYN() {
        return this.autreYN;
    }

    public Pays autreYN(Boolean autreYN) {
        this.setAutreYN(autreYN);
        return this;
    }

    public void setAutreYN(Boolean autreYN) {
        this.autreYN = autreYN;
    }

    public Boolean getEstEtrangerYN() {
        return this.estEtrangerYN;
    }

    public Pays estEtrangerYN(Boolean estEtrangerYN) {
        this.setEstEtrangerYN(estEtrangerYN);
        return this;
    }

    public void setEstEtrangerYN(Boolean estEtrangerYN) {
        this.estEtrangerYN = estEtrangerYN;
    }

    public Set<Zone> getZones() {
        return this.zones;
    }

    public void setZones(Set<Zone> zones) {
        this.zones = zones;
    }

    public Pays zones(Set<Zone> zones) {
        this.setZones(zones);
        return this;
    }

    public Pays addZone(Zone zone) {
        this.zones.add(zone);
        return this;
    }

    public Pays removeZone(Zone zone) {
        this.zones.remove(zone);
        return this;
    }

    public Set<Region> getRegions() {
        return this.regions;
    }

    public void setRegions(Set<Region> regions) {
        if (this.regions != null) {
            this.regions.forEach(i -> i.setPays(null));
        }
        if (regions != null) {
            regions.forEach(i -> i.setPays(this));
        }
        this.regions = regions;
    }

    public Pays regions(Set<Region> regions) {
        this.setRegions(regions);
        return this;
    }

    public Pays addRegions(Region region) {
        this.regions.add(region);
        region.setPays(this);
        return this;
    }

    public Pays removeRegions(Region region) {
        this.regions.remove(region);
        region.setPays(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pays)) {
            return false;
        }
        return getId() != null && getId().equals(((Pays) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pays{" +
            "id=" + getId() +
            ", libellePays='" + getLibellePays() + "'" +
            ", paysEnAnglais='" + getPaysEnAnglais() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", codePays='" + getCodePays() + "'" +
            ", uEMOAYN='" + getuEMOAYN() + "'" +
            ", cEDEAOYN='" + getcEDEAOYN() + "'" +
            ", rIMYN='" + getrIMYN() + "'" +
            ", autreYN='" + getAutreYN() + "'" +
            ", estEtrangerYN='" + getEstEtrangerYN() + "'" +
            "}";
    }
}
