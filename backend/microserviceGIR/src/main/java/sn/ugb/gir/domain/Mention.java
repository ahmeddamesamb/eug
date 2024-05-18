package sn.ugb.gir.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mention.
 */
@Entity
@Table(name = "mention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_mention")
    private String libelleMention;

    @ManyToOne(fetch = FetchType.LAZY)
    private Domaine domaine;

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
            "}";
    }
}
