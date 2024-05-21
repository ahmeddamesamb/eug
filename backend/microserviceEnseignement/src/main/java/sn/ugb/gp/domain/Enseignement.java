package sn.ugb.gp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enseignement.
 */
@Entity
@Table(name = "enseignement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enseignement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_enseignements")
    private String libelleEnseignements;

    @Column(name = "volume_horaire")
    private Float volumeHoraire;

    @Column(name = "nombre_inscrits")
    private Integer nombreInscrits;

    @Column(name = "groupe_yn")
    private Integer groupeYN;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enseignement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleEnseignements() {
        return this.libelleEnseignements;
    }

    public Enseignement libelleEnseignements(String libelleEnseignements) {
        this.setLibelleEnseignements(libelleEnseignements);
        return this;
    }

    public void setLibelleEnseignements(String libelleEnseignements) {
        this.libelleEnseignements = libelleEnseignements;
    }

    public Float getVolumeHoraire() {
        return this.volumeHoraire;
    }

    public Enseignement volumeHoraire(Float volumeHoraire) {
        this.setVolumeHoraire(volumeHoraire);
        return this;
    }

    public void setVolumeHoraire(Float volumeHoraire) {
        this.volumeHoraire = volumeHoraire;
    }

    public Integer getNombreInscrits() {
        return this.nombreInscrits;
    }

    public Enseignement nombreInscrits(Integer nombreInscrits) {
        this.setNombreInscrits(nombreInscrits);
        return this;
    }

    public void setNombreInscrits(Integer nombreInscrits) {
        this.nombreInscrits = nombreInscrits;
    }

    public Integer getGroupeYN() {
        return this.groupeYN;
    }

    public Enseignement groupeYN(Integer groupeYN) {
        this.setGroupeYN(groupeYN);
        return this;
    }

    public void setGroupeYN(Integer groupeYN) {
        this.groupeYN = groupeYN;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enseignement)) {
            return false;
        }
        return getId() != null && getId().equals(((Enseignement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enseignement{" +
            "id=" + getId() +
            ", libelleEnseignements='" + getLibelleEnseignements() + "'" +
            ", volumeHoraire=" + getVolumeHoraire() +
            ", nombreInscrits=" + getNombreInscrits() +
            ", groupeYN=" + getGroupeYN() +
            "}";
    }
}
