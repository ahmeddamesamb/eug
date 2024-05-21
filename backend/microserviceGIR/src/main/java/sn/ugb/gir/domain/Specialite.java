package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Specialite.
 */
@Entity
@Table(name = "specialite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_specialites")
    private String nomSpecialites;

    @Column(name = "sigle_specialites")
    private String sigleSpecialites;

    @Column(name = "specialite_particulier_yn")
    private Integer specialiteParticulierYN;

    @Column(name = "specialites_payante_yn")
    private Integer specialitesPayanteYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "domaine" }, allowSetters = true)
    private Mention mention;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Specialite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSpecialites() {
        return this.nomSpecialites;
    }

    public Specialite nomSpecialites(String nomSpecialites) {
        this.setNomSpecialites(nomSpecialites);
        return this;
    }

    public void setNomSpecialites(String nomSpecialites) {
        this.nomSpecialites = nomSpecialites;
    }

    public String getSigleSpecialites() {
        return this.sigleSpecialites;
    }

    public Specialite sigleSpecialites(String sigleSpecialites) {
        this.setSigleSpecialites(sigleSpecialites);
        return this;
    }

    public void setSigleSpecialites(String sigleSpecialites) {
        this.sigleSpecialites = sigleSpecialites;
    }

    public Integer getSpecialiteParticulierYN() {
        return this.specialiteParticulierYN;
    }

    public Specialite specialiteParticulierYN(Integer specialiteParticulierYN) {
        this.setSpecialiteParticulierYN(specialiteParticulierYN);
        return this;
    }

    public void setSpecialiteParticulierYN(Integer specialiteParticulierYN) {
        this.specialiteParticulierYN = specialiteParticulierYN;
    }

    public Integer getSpecialitesPayanteYN() {
        return this.specialitesPayanteYN;
    }

    public Specialite specialitesPayanteYN(Integer specialitesPayanteYN) {
        this.setSpecialitesPayanteYN(specialitesPayanteYN);
        return this;
    }

    public void setSpecialitesPayanteYN(Integer specialitesPayanteYN) {
        this.specialitesPayanteYN = specialitesPayanteYN;
    }

    public Mention getMention() {
        return this.mention;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }

    public Specialite mention(Mention mention) {
        this.setMention(mention);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialite)) {
            return false;
        }
        return getId() != null && getId().equals(((Specialite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialite{" +
            "id=" + getId() +
            ", nomSpecialites='" + getNomSpecialites() + "'" +
            ", sigleSpecialites='" + getSigleSpecialites() + "'" +
            ", specialiteParticulierYN=" + getSpecialiteParticulierYN() +
            ", specialitesPayanteYN=" + getSpecialitesPayanteYN() +
            "}";
    }
}
