package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AnneeAcademique.
 */
@Entity
@Table(name = "annee_academique", uniqueConstraints = {
    @UniqueConstraint(columnNames = "annee_courante_yn")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)


@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnneeAcademique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^\\d{4}[-/]\\d{4}$", message = "Le libellé de l'anneé   rttyyy  academique doit être au format '2000-2001' ou '2000/2001'")
    @Column(name = "libelle_annee_academique" , nullable = false)
    private String libelleAnneeAcademique;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "L'anneé academique doit être au format 'aaaa' ")
    @Column(name = "annee_ac",  nullable = false, unique = true )
    private String anneeAc;


    @NotNull
    @Column(name = "annee_courante_yn", nullable = false)
    private Integer anneeCouranteYN;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnneeAcademique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleAnneeAcademique() {
        return this.libelleAnneeAcademique;
    }

    public AnneeAcademique libelleAnneeAcademique(String libelleAnneeAcademique) {
        this.setLibelleAnneeAcademique(libelleAnneeAcademique);
        return this;
    }

    public void setLibelleAnneeAcademique(String libelleAnneeAcademique) {
        this.libelleAnneeAcademique = libelleAnneeAcademique;
    }

    public String getAnneeAc() {
        return this.anneeAc;
    }

    public AnneeAcademique anneeAc(String anneeAc) {
        this.setAnneeAc(anneeAc);
        return this;
    }

    public void setAnneeAc(String anneeAc) {
        this.anneeAc = anneeAc;
    }

    public Integer getAnneeCouranteYN() {
        return this.anneeCouranteYN;
    }

    public AnneeAcademique anneeCouranteYN(Integer anneeCouranteYN) {
        this.setAnneeCouranteYN(anneeCouranteYN);
        return this;
    }

    public void setAnneeCouranteYN(Integer anneeCouranteYN) {
        this.anneeCouranteYN = anneeCouranteYN;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnneeAcademique)) {
            return false;
        }
        return getId() != null && getId().equals(((AnneeAcademique) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnneeAcademique{" +
            "id=" + getId() +
            ", libelleAnneeAcademique='" + getLibelleAnneeAcademique() + "'" +
            ", anneeAc='" + getAnneeAc() + "'" +
            ", anneeCouranteYN=" + getAnneeCouranteYN() +
            "}";
    }
}
