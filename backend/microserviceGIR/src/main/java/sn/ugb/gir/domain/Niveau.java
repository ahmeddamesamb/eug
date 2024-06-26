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
 * A Niveau.
 */
@Entity
@Table(name = "niveau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "niveau")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Niveau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_niveau", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleNiveau;

    @NotNull
    @Column(name = "code_niveau", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codeNiveau;

    @Column(name = "annee_etude")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String anneeEtude;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "niveaux", "frais" }, allowSetters = true)
    private Cycle typeCycle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "niveau")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
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

    public Niveau id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleNiveau() {
        return this.libelleNiveau;
    }

    public Niveau libelleNiveau(String libelleNiveau) {
        this.setLibelleNiveau(libelleNiveau);
        return this;
    }

    public void setLibelleNiveau(String libelleNiveau) {
        this.libelleNiveau = libelleNiveau;
    }

    public String getCodeNiveau() {
        return this.codeNiveau;
    }

    public Niveau codeNiveau(String codeNiveau) {
        this.setCodeNiveau(codeNiveau);
        return this;
    }

    public void setCodeNiveau(String codeNiveau) {
        this.codeNiveau = codeNiveau;
    }

    public String getAnneeEtude() {
        return this.anneeEtude;
    }

    public Niveau anneeEtude(String anneeEtude) {
        this.setAnneeEtude(anneeEtude);
        return this;
    }

    public void setAnneeEtude(String anneeEtude) {
        this.anneeEtude = anneeEtude;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Niveau actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Cycle getTypeCycle() {
        return this.typeCycle;
    }

    public void setTypeCycle(Cycle cycle) {
        this.typeCycle = cycle;
    }

    public Niveau typeCycle(Cycle cycle) {
        this.setTypeCycle(cycle);
        return this;
    }

    public Set<Formation> getFormations() {
        return this.formations;
    }

    public void setFormations(Set<Formation> formations) {
        if (this.formations != null) {
            this.formations.forEach(i -> i.setNiveau(null));
        }
        if (formations != null) {
            formations.forEach(i -> i.setNiveau(this));
        }
        this.formations = formations;
    }

    public Niveau formations(Set<Formation> formations) {
        this.setFormations(formations);
        return this;
    }

    public Niveau addFormations(Formation formation) {
        this.formations.add(formation);
        formation.setNiveau(this);
        return this;
    }

    public Niveau removeFormations(Formation formation) {
        this.formations.remove(formation);
        formation.setNiveau(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Niveau)) {
            return false;
        }
        return getId() != null && getId().equals(((Niveau) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Niveau{" +
            "id=" + getId() +
            ", libelleNiveau='" + getLibelleNiveau() + "'" +
            ", codeNiveau='" + getCodeNiveau() + "'" +
            ", anneeEtude='" + getAnneeEtude() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
