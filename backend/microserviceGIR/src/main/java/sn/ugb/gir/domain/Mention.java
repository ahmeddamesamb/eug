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
 * A Mention.
 */
@Entity
@Table(name = "mention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "mention")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_mention", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleMention;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ufrs", "mentions" }, allowSetters = true)
    private Domaine domaine;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mention")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "mention", "formations" }, allowSetters = true)
    private Set<Specialite> specialites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleMention() {
        return this.libelleMention;
    }

    public Mention libelleMention(String libelleMention) {
        this.setLibelleMention(libelleMention);
        return this;
    }

    public void setLibelleMention(String libelleMention) {
        this.libelleMention = libelleMention;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Mention actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Domaine getDomaine() {
        return this.domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public Mention domaine(Domaine domaine) {
        this.setDomaine(domaine);
        return this;
    }

    public Set<Specialite> getSpecialites() {
        return this.specialites;
    }

    public void setSpecialites(Set<Specialite> specialites) {
        if (this.specialites != null) {
            this.specialites.forEach(i -> i.setMention(null));
        }
        if (specialites != null) {
            specialites.forEach(i -> i.setMention(this));
        }
        this.specialites = specialites;
    }

    public Mention specialites(Set<Specialite> specialites) {
        this.setSpecialites(specialites);
        return this;
    }

    public Mention addSpecialites(Specialite specialite) {
        this.specialites.add(specialite);
        specialite.setMention(this);
        return this;
    }

    public Mention removeSpecialites(Specialite specialite) {
        this.specialites.remove(specialite);
        specialite.setMention(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mention)) {
            return false;
        }
        return getId() != null && getId().equals(((Mention) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mention{" +
            "id=" + getId() +
            ", libelleMention='" + getLibelleMention() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
