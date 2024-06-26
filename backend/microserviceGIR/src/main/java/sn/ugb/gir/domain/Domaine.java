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
 * A Domaine.
 */
@Entity
@Table(name = "domaine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "domaine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Domaine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_domaine", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleDomaine;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_domaine__ufr", joinColumns = @JoinColumn(name = "domaine_id"), inverseJoinColumns = @JoinColumn(name = "ufr_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "universite", "domaines" }, allowSetters = true)
    private Set<Ufr> ufrs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "domaine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "domaine", "specialites" }, allowSetters = true)
    private Set<Mention> mentions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Domaine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleDomaine() {
        return this.libelleDomaine;
    }

    public Domaine libelleDomaine(String libelleDomaine) {
        this.setLibelleDomaine(libelleDomaine);
        return this;
    }

    public void setLibelleDomaine(String libelleDomaine) {
        this.libelleDomaine = libelleDomaine;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Domaine actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<Ufr> getUfrs() {
        return this.ufrs;
    }

    public void setUfrs(Set<Ufr> ufrs) {
        this.ufrs = ufrs;
    }

    public Domaine ufrs(Set<Ufr> ufrs) {
        this.setUfrs(ufrs);
        return this;
    }

    public Domaine addUfr(Ufr ufr) {
        this.ufrs.add(ufr);
        return this;
    }

    public Domaine removeUfr(Ufr ufr) {
        this.ufrs.remove(ufr);
        return this;
    }

    public Set<Mention> getMentions() {
        return this.mentions;
    }

    public void setMentions(Set<Mention> mentions) {
        if (this.mentions != null) {
            this.mentions.forEach(i -> i.setDomaine(null));
        }
        if (mentions != null) {
            mentions.forEach(i -> i.setDomaine(this));
        }
        this.mentions = mentions;
    }

    public Domaine mentions(Set<Mention> mentions) {
        this.setMentions(mentions);
        return this;
    }

    public Domaine addMentions(Mention mention) {
        this.mentions.add(mention);
        mention.setDomaine(this);
        return this;
    }

    public Domaine removeMentions(Mention mention) {
        this.mentions.remove(mention);
        mention.setDomaine(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Domaine)) {
            return false;
        }
        return getId() != null && getId().equals(((Domaine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Domaine{" +
            "id=" + getId() +
            ", libelleDomaine='" + getLibelleDomaine() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
