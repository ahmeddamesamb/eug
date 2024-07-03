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
 * A Lycee.
 */
@Entity
@Table(name = "lycee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "lycee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lycee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_lycee", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomLycee;

    @NotNull //Ajoute
    @Column(name = "code_lycee")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codeLycee;

    @Column(name = "ville_lycee")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String villeLycee;

    @Column(name = "academie_lycee")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer academieLycee;

    @Column(name = "centre_examen")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String centreExamen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pays", "etudiants", "lycees" }, allowSetters = true)
    private Region region;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lycee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "region",
            "typeSelection",
            "lycee",
            "informationPersonnelle",
            "baccalaureat",
            "disciplineSportiveEtudiants",
            "inscriptionAdministratives",
        },
        allowSetters = true
    )
    private Set<Etudiant> etudiants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lycee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLycee() {
        return this.nomLycee;
    }

    public Lycee nomLycee(String nomLycee) {
        this.setNomLycee(nomLycee);
        return this;
    }

    public void setNomLycee(String nomLycee) {
        this.nomLycee = nomLycee;
    }

    public String getCodeLycee() {
        return this.codeLycee;
    }

    public Lycee codeLycee(String codeLycee) {
        this.setCodeLycee(codeLycee);
        return this;
    }

    public void setCodeLycee(String codeLycee) {
        this.codeLycee = codeLycee;
    }

    public String getVilleLycee() {
        return this.villeLycee;
    }

    public Lycee villeLycee(String villeLycee) {
        this.setVilleLycee(villeLycee);
        return this;
    }

    public void setVilleLycee(String villeLycee) {
        this.villeLycee = villeLycee;
    }

    public Integer getAcademieLycee() {
        return this.academieLycee;
    }

    public Lycee academieLycee(Integer academieLycee) {
        this.setAcademieLycee(academieLycee);
        return this;
    }

    public void setAcademieLycee(Integer academieLycee) {
        this.academieLycee = academieLycee;
    }

    public String getCentreExamen() {
        return this.centreExamen;
    }

    public Lycee centreExamen(String centreExamen) {
        this.setCentreExamen(centreExamen);
        return this;
    }

    public void setCentreExamen(String centreExamen) {
        this.centreExamen = centreExamen;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Lycee region(Region region) {
        this.setRegion(region);
        return this;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.setLycee(null));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.setLycee(this));
        }
        this.etudiants = etudiants;
    }

    public Lycee etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Lycee addEtudiants(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setLycee(this);
        return this;
    }

    public Lycee removeEtudiants(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setLycee(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lycee)) {
            return false;
        }
        return getId() != null && getId().equals(((Lycee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lycee{" +
            "id=" + getId() +
            ", nomLycee='" + getNomLycee() + "'" +
            ", codeLycee='" + getCodeLycee() + "'" +
            ", villeLycee='" + getVilleLycee() + "'" +
            ", academieLycee=" + getAcademieLycee() +
            ", centreExamen='" + getCentreExamen() + "'" +
            "}";
    }
}
