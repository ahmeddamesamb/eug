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
 * A FormationAutorisee.
 */
@Entity
@Table(name = "formation_autorisee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formationautorisee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationAutorisee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "en_cours_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean enCoursYN;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_formation_autorisee__formation",
        joinColumns = @JoinColumn(name = "formation_autorisee_id"),
        inverseJoinColumns = @JoinColumn(name = "formation_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "typeFormation",
            "niveau",
            "specialite",
            "departement",
            "formationPrivee",
            "formationInvalides",
            "inscriptionAdministrativeFormations",
            "programmationInscriptions",
            "formationAutorisees",
        },
        allowSetters = true
    )
    private Set<Formation> formations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormationAutorisee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public FormationAutorisee dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public FormationAutorisee dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getEnCoursYN() {
        return this.enCoursYN;
    }

    public FormationAutorisee enCoursYN(Boolean enCoursYN) {
        this.setEnCoursYN(enCoursYN);
        return this;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public FormationAutorisee actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<Formation> getFormations() {
        return this.formations;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }

    public FormationAutorisee formations(Set<Formation> formations) {
        this.setFormations(formations);
        return this;
    }

    public FormationAutorisee addFormation(Formation formation) {
        this.formations.add(formation);
        return this;
    }

    public FormationAutorisee removeFormation(Formation formation) {
        this.formations.remove(formation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationAutorisee)) {
            return false;
        }
        return getId() != null && getId().equals(((FormationAutorisee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationAutorisee{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
