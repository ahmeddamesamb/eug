package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_mensualites")
    private Integer nombreMensualites;

    @Column(name = "frais_dossier_yn")
    private Integer fraisDossierYN;

    @Column(name = "classe_diplomante_yn")
    private Integer classeDiplomanteYN;

    @Column(name = "libelle_diplome")
    private String libelleDiplome;

    @Column(name = "code_formation")
    private String codeFormation;

    @Column(name = "nbre_credits_min")
    private Integer nbreCreditsMin;

    @Column(name = "est_parcours_yn")
    private Integer estParcoursYN;

    @Column(name = "lmd_yn")
    private Integer lmdYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mention" }, allowSetters = true)
    private Specialite specialite;

    @ManyToOne(fetch = FetchType.LAZY)
    private Niveau niveau;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "formations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "formations" }, allowSetters = true)
    private Set<FormationAutorisee> formationAutorisees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Formation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNombreMensualites() {
        return this.nombreMensualites;
    }

    public Formation nombreMensualites(Integer nombreMensualites) {
        this.setNombreMensualites(nombreMensualites);
        return this;
    }

    public void setNombreMensualites(Integer nombreMensualites) {
        this.nombreMensualites = nombreMensualites;
    }

    public Integer getFraisDossierYN() {
        return this.fraisDossierYN;
    }

    public Formation fraisDossierYN(Integer fraisDossierYN) {
        this.setFraisDossierYN(fraisDossierYN);
        return this;
    }

    public void setFraisDossierYN(Integer fraisDossierYN) {
        this.fraisDossierYN = fraisDossierYN;
    }

    public Integer getClasseDiplomanteYN() {
        return this.classeDiplomanteYN;
    }

    public Formation classeDiplomanteYN(Integer classeDiplomanteYN) {
        this.setClasseDiplomanteYN(classeDiplomanteYN);
        return this;
    }

    public void setClasseDiplomanteYN(Integer classeDiplomanteYN) {
        this.classeDiplomanteYN = classeDiplomanteYN;
    }

    public String getLibelleDiplome() {
        return this.libelleDiplome;
    }

    public Formation libelleDiplome(String libelleDiplome) {
        this.setLibelleDiplome(libelleDiplome);
        return this;
    }

    public void setLibelleDiplome(String libelleDiplome) {
        this.libelleDiplome = libelleDiplome;
    }

    public String getCodeFormation() {
        return this.codeFormation;
    }

    public Formation codeFormation(String codeFormation) {
        this.setCodeFormation(codeFormation);
        return this;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
    }

    public Integer getNbreCreditsMin() {
        return this.nbreCreditsMin;
    }

    public Formation nbreCreditsMin(Integer nbreCreditsMin) {
        this.setNbreCreditsMin(nbreCreditsMin);
        return this;
    }

    public void setNbreCreditsMin(Integer nbreCreditsMin) {
        this.nbreCreditsMin = nbreCreditsMin;
    }

    public Integer getEstParcoursYN() {
        return this.estParcoursYN;
    }

    public Formation estParcoursYN(Integer estParcoursYN) {
        this.setEstParcoursYN(estParcoursYN);
        return this;
    }

    public void setEstParcoursYN(Integer estParcoursYN) {
        this.estParcoursYN = estParcoursYN;
    }

    public Integer getLmdYN() {
        return this.lmdYN;
    }

    public Formation lmdYN(Integer lmdYN) {
        this.setLmdYN(lmdYN);
        return this;
    }

    public void setLmdYN(Integer lmdYN) {
        this.lmdYN = lmdYN;
    }

    public Specialite getSpecialite() {
        return this.specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public Formation specialite(Specialite specialite) {
        this.setSpecialite(specialite);
        return this;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Formation niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public Set<FormationAutorisee> getFormationAutorisees() {
        return this.formationAutorisees;
    }

    public void setFormationAutorisees(Set<FormationAutorisee> formationAutorisees) {
        if (this.formationAutorisees != null) {
            this.formationAutorisees.forEach(i -> i.removeFormation(this));
        }
        if (formationAutorisees != null) {
            formationAutorisees.forEach(i -> i.addFormation(this));
        }
        this.formationAutorisees = formationAutorisees;
    }

    public Formation formationAutorisees(Set<FormationAutorisee> formationAutorisees) {
        this.setFormationAutorisees(formationAutorisees);
        return this;
    }

    public Formation addFormationAutorisee(FormationAutorisee formationAutorisee) {
        this.formationAutorisees.add(formationAutorisee);
        formationAutorisee.getFormations().add(this);
        return this;
    }

    public Formation removeFormationAutorisee(FormationAutorisee formationAutorisee) {
        this.formationAutorisees.remove(formationAutorisee);
        formationAutorisee.getFormations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formation)) {
            return false;
        }
        return getId() != null && getId().equals(((Formation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formation{" +
            "id=" + getId() +
            ", nombreMensualites=" + getNombreMensualites() +
            ", fraisDossierYN=" + getFraisDossierYN() +
            ", classeDiplomanteYN=" + getClasseDiplomanteYN() +
            ", libelleDiplome='" + getLibelleDiplome() + "'" +
            ", codeFormation='" + getCodeFormation() + "'" +
            ", nbreCreditsMin=" + getNbreCreditsMin() +
            ", estParcoursYN=" + getEstParcoursYN() +
            ", lmdYN=" + getLmdYN() +
            "}";
    }
}
