package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Campagne.
 */
@Entity
@Table(name = "campagne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "campagne")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Campagne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_campagne")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleCampagne;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "libelle_abrege")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleAbrege;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campagne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "anneeAcademique", "formation", "campagne" }, allowSetters = true)
    private Set<ProgrammationInscription> programmationInscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Campagne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleCampagne() {
        return this.libelleCampagne;
    }

    public Campagne libelleCampagne(String libelleCampagne) {
        this.setLibelleCampagne(libelleCampagne);
        return this;
    }

    public void setLibelleCampagne(String libelleCampagne) {
        this.libelleCampagne = libelleCampagne;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Campagne dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Campagne dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getLibelleAbrege() {
        return this.libelleAbrege;
    }

    public Campagne libelleAbrege(String libelleAbrege) {
        this.setLibelleAbrege(libelleAbrege);
        return this;
    }

    public void setLibelleAbrege(String libelleAbrege) {
        this.libelleAbrege = libelleAbrege;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Campagne actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<ProgrammationInscription> getProgrammationInscriptions() {
        return this.programmationInscriptions;
    }

    public void setProgrammationInscriptions(Set<ProgrammationInscription> programmationInscriptions) {
        if (this.programmationInscriptions != null) {
            this.programmationInscriptions.forEach(i -> i.setCampagne(null));
        }
        if (programmationInscriptions != null) {
            programmationInscriptions.forEach(i -> i.setCampagne(this));
        }
        this.programmationInscriptions = programmationInscriptions;
    }

    public Campagne programmationInscriptions(Set<ProgrammationInscription> programmationInscriptions) {
        this.setProgrammationInscriptions(programmationInscriptions);
        return this;
    }

    public Campagne addProgrammationInscriptions(ProgrammationInscription programmationInscription) {
        this.programmationInscriptions.add(programmationInscription);
        programmationInscription.setCampagne(this);
        return this;
    }

    public Campagne removeProgrammationInscriptions(ProgrammationInscription programmationInscription) {
        this.programmationInscriptions.remove(programmationInscription);
        programmationInscription.setCampagne(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campagne)) {
            return false;
        }
        return getId() != null && getId().equals(((Campagne) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campagne{" +
            "id=" + getId() +
            ", libelleCampagne='" + getLibelleCampagne() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", libelleAbrege='" + getLibelleAbrege() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
