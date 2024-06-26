package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etudiant.
 */
@Entity
@Table(name = "etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etudiant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_etu", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codeEtu;

    @Column(name = "ine")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ine;

    @Column(name = "code_bu")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codeBU;

    @Column(name = "email_ugb")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String emailUGB;

    @NotNull
    @Column(name = "date_naiss_etu", nullable = false)
    private LocalDate dateNaissEtu;

    @NotNull
    @Column(name = "lieu_naiss_etu", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lieuNaissEtu;

    @NotNull
    @Column(name = "sexe", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sexe;

    @Column(name = "num_doc_identite")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String numDocIdentite;

    @NotNull
    @Column(name = "assimile_yn", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean assimileYN;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pays", "etudiants", "lycees" }, allowSetters = true)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "etudiants" }, allowSetters = true)
    private TypeSelection typeSelection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "region", "etudiants" }, allowSetters = true)
    private Lycee lycee;

    @JsonIgnoreProperties(value = { "etudiant", "typeHandicap", "typeBourse" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @org.springframework.data.annotation.Transient
    private InformationPersonnelle informationPersonnelle;

    @JsonIgnoreProperties(value = { "etudiant", "serie" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @org.springframework.data.annotation.Transient
    private Baccalaureat baccalaureat;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "disciplineSportive", "etudiant" }, allowSetters = true)
    private Set<DisciplineSportiveEtudiant> disciplineSportiveEtudiants = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "typeAdmission", "anneeAcademique", "etudiant", "processusDinscriptionAdministrative", "inscriptionAdministrativeFormations",
        },
        allowSetters = true
    )
    private Set<InscriptionAdministrative> inscriptionAdministratives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeEtu() {
        return this.codeEtu;
    }

    public Etudiant codeEtu(String codeEtu) {
        this.setCodeEtu(codeEtu);
        return this;
    }

    public void setCodeEtu(String codeEtu) {
        this.codeEtu = codeEtu;
    }

    public String getIne() {
        return this.ine;
    }

    public Etudiant ine(String ine) {
        this.setIne(ine);
        return this;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public String getCodeBU() {
        return this.codeBU;
    }

    public Etudiant codeBU(String codeBU) {
        this.setCodeBU(codeBU);
        return this;
    }

    public void setCodeBU(String codeBU) {
        this.codeBU = codeBU;
    }

    public String getEmailUGB() {
        return this.emailUGB;
    }

    public Etudiant emailUGB(String emailUGB) {
        this.setEmailUGB(emailUGB);
        return this;
    }

    public void setEmailUGB(String emailUGB) {
        this.emailUGB = emailUGB;
    }

    public LocalDate getDateNaissEtu() {
        return this.dateNaissEtu;
    }

    public Etudiant dateNaissEtu(LocalDate dateNaissEtu) {
        this.setDateNaissEtu(dateNaissEtu);
        return this;
    }

    public void setDateNaissEtu(LocalDate dateNaissEtu) {
        this.dateNaissEtu = dateNaissEtu;
    }

    public String getLieuNaissEtu() {
        return this.lieuNaissEtu;
    }

    public Etudiant lieuNaissEtu(String lieuNaissEtu) {
        this.setLieuNaissEtu(lieuNaissEtu);
        return this;
    }

    public void setLieuNaissEtu(String lieuNaissEtu) {
        this.lieuNaissEtu = lieuNaissEtu;
    }

    public String getSexe() {
        return this.sexe;
    }

    public Etudiant sexe(String sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNumDocIdentite() {
        return this.numDocIdentite;
    }

    public Etudiant numDocIdentite(String numDocIdentite) {
        this.setNumDocIdentite(numDocIdentite);
        return this;
    }

    public void setNumDocIdentite(String numDocIdentite) {
        this.numDocIdentite = numDocIdentite;
    }

    public Boolean getAssimileYN() {
        return this.assimileYN;
    }

    public Etudiant assimileYN(Boolean assimileYN) {
        this.setAssimileYN(assimileYN);
        return this;
    }

    public void setAssimileYN(Boolean assimileYN) {
        this.assimileYN = assimileYN;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Etudiant actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Etudiant region(Region region) {
        this.setRegion(region);
        return this;
    }

    public TypeSelection getTypeSelection() {
        return this.typeSelection;
    }

    public void setTypeSelection(TypeSelection typeSelection) {
        this.typeSelection = typeSelection;
    }

    public Etudiant typeSelection(TypeSelection typeSelection) {
        this.setTypeSelection(typeSelection);
        return this;
    }

    public Lycee getLycee() {
        return this.lycee;
    }

    public void setLycee(Lycee lycee) {
        this.lycee = lycee;
    }

    public Etudiant lycee(Lycee lycee) {
        this.setLycee(lycee);
        return this;
    }

    public InformationPersonnelle getInformationPersonnelle() {
        return this.informationPersonnelle;
    }

    public void setInformationPersonnelle(InformationPersonnelle informationPersonnelle) {
        if (this.informationPersonnelle != null) {
            this.informationPersonnelle.setEtudiant(null);
        }
        if (informationPersonnelle != null) {
            informationPersonnelle.setEtudiant(this);
        }
        this.informationPersonnelle = informationPersonnelle;
    }

    public Etudiant informationPersonnelle(InformationPersonnelle informationPersonnelle) {
        this.setInformationPersonnelle(informationPersonnelle);
        return this;
    }

    public Baccalaureat getBaccalaureat() {
        return this.baccalaureat;
    }

    public void setBaccalaureat(Baccalaureat baccalaureat) {
        if (this.baccalaureat != null) {
            this.baccalaureat.setEtudiant(null);
        }
        if (baccalaureat != null) {
            baccalaureat.setEtudiant(this);
        }
        this.baccalaureat = baccalaureat;
    }

    public Etudiant baccalaureat(Baccalaureat baccalaureat) {
        this.setBaccalaureat(baccalaureat);
        return this;
    }

    public Set<DisciplineSportiveEtudiant> getDisciplineSportiveEtudiants() {
        return this.disciplineSportiveEtudiants;
    }

    public void setDisciplineSportiveEtudiants(Set<DisciplineSportiveEtudiant> disciplineSportiveEtudiants) {
        if (this.disciplineSportiveEtudiants != null) {
            this.disciplineSportiveEtudiants.forEach(i -> i.setEtudiant(null));
        }
        if (disciplineSportiveEtudiants != null) {
            disciplineSportiveEtudiants.forEach(i -> i.setEtudiant(this));
        }
        this.disciplineSportiveEtudiants = disciplineSportiveEtudiants;
    }

    public Etudiant disciplineSportiveEtudiants(Set<DisciplineSportiveEtudiant> disciplineSportiveEtudiants) {
        this.setDisciplineSportiveEtudiants(disciplineSportiveEtudiants);
        return this;
    }

    public Etudiant addDisciplineSportiveEtudiants(DisciplineSportiveEtudiant disciplineSportiveEtudiant) {
        this.disciplineSportiveEtudiants.add(disciplineSportiveEtudiant);
        disciplineSportiveEtudiant.setEtudiant(this);
        return this;
    }

    public Etudiant removeDisciplineSportiveEtudiants(DisciplineSportiveEtudiant disciplineSportiveEtudiant) {
        this.disciplineSportiveEtudiants.remove(disciplineSportiveEtudiant);
        disciplineSportiveEtudiant.setEtudiant(null);
        return this;
    }

    public Set<InscriptionAdministrative> getInscriptionAdministratives() {
        return this.inscriptionAdministratives;
    }

    public void setInscriptionAdministratives(Set<InscriptionAdministrative> inscriptionAdministratives) {
        if (this.inscriptionAdministratives != null) {
            this.inscriptionAdministratives.forEach(i -> i.setEtudiant(null));
        }
        if (inscriptionAdministratives != null) {
            inscriptionAdministratives.forEach(i -> i.setEtudiant(this));
        }
        this.inscriptionAdministratives = inscriptionAdministratives;
    }

    public Etudiant inscriptionAdministratives(Set<InscriptionAdministrative> inscriptionAdministratives) {
        this.setInscriptionAdministratives(inscriptionAdministratives);
        return this;
    }

    public Etudiant addInscriptionAdministratives(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministratives.add(inscriptionAdministrative);
        inscriptionAdministrative.setEtudiant(this);
        return this;
    }

    public Etudiant removeInscriptionAdministratives(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministratives.remove(inscriptionAdministrative);
        inscriptionAdministrative.setEtudiant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((Etudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + getId() +
            ", codeEtu='" + getCodeEtu() + "'" +
            ", ine='" + getIne() + "'" +
            ", codeBU='" + getCodeBU() + "'" +
            ", emailUGB='" + getEmailUGB() + "'" +
            ", dateNaissEtu='" + getDateNaissEtu() + "'" +
            ", lieuNaissEtu='" + getLieuNaissEtu() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", numDocIdentite='" + getNumDocIdentite() + "'" +
            ", assimileYN='" + getAssimileYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
