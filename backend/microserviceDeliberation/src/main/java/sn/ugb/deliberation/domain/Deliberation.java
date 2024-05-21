package sn.ugb.deliberation.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deliberation.
 */
@Entity
@Table(name = "deliberation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Deliberation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "est_validee")
    private Integer estValidee;

    @Lob
    @Column(name = "pv_deliberation")
    private byte[] pvDeliberation;

    @Column(name = "pv_deliberation_content_type")
    private String pvDeliberationContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deliberation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstValidee() {
        return this.estValidee;
    }

    public Deliberation estValidee(Integer estValidee) {
        this.setEstValidee(estValidee);
        return this;
    }

    public void setEstValidee(Integer estValidee) {
        this.estValidee = estValidee;
    }

    public byte[] getPvDeliberation() {
        return this.pvDeliberation;
    }

    public Deliberation pvDeliberation(byte[] pvDeliberation) {
        this.setPvDeliberation(pvDeliberation);
        return this;
    }

    public void setPvDeliberation(byte[] pvDeliberation) {
        this.pvDeliberation = pvDeliberation;
    }

    public String getPvDeliberationContentType() {
        return this.pvDeliberationContentType;
    }

    public Deliberation pvDeliberationContentType(String pvDeliberationContentType) {
        this.pvDeliberationContentType = pvDeliberationContentType;
        return this;
    }

    public void setPvDeliberationContentType(String pvDeliberationContentType) {
        this.pvDeliberationContentType = pvDeliberationContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deliberation)) {
            return false;
        }
        return getId() != null && getId().equals(((Deliberation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deliberation{" +
            "id=" + getId() +
            ", estValidee=" + getEstValidee() +
            ", pvDeliberation='" + getPvDeliberation() + "'" +
            ", pvDeliberationContentType='" + getPvDeliberationContentType() + "'" +
            "}";
    }
}
