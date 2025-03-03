package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ministere.
 */
@Entity
@Table(name = "ministere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ministere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_ministere", nullable = false, unique = true)
    private String nomMinistere;

    @Column(name = "sigle_ministere")
    private String sigleMinistere;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull
    @Column(name = "en_cours_yn", nullable = false)
    private Integer enCoursYN;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ministere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMinistere() {
        return this.nomMinistere;
    }

    public Ministere nomMinistere(String nomMinistere) {
        this.setNomMinistere(nomMinistere);
        return this;
    }

    public void setNomMinistere(String nomMinistere) {
        this.nomMinistere = nomMinistere;
    }

    public String getSigleMinistere() {
        return this.sigleMinistere;
    }

    public Ministere sigleMinistere(String sigleMinistere) {
        this.setSigleMinistere(sigleMinistere);
        return this;
    }

    public void setSigleMinistere(String sigleMinistere) {
        this.sigleMinistere = sigleMinistere;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Ministere dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Ministere dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getEnCoursYN() {
        return this.enCoursYN;
    }

    public Ministere enCoursYN(Integer enCoursYN) {
        this.setEnCoursYN(enCoursYN);
        return this;
    }

    public void setEnCoursYN(Integer enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ministere)) {
            return false;
        }
        return getId() != null && getId().equals(((Ministere) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ministere{" +
            "id=" + getId() +
            ", nomMinistere='" + getNomMinistere() + "'" +
            ", sigleMinistere='" + getSigleMinistere() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCoursYN=" + getEnCoursYN() +
            "}";
    }
}
