package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InscriptionAdministrative.
 */
@Entity
@Table(name = "inscription_administrative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionAdministrative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nouveau_inscrit_yn")
    private Integer nouveauInscritYN;

    @Column(name = "reprise_yn")
    private Integer repriseYN;

    @Column(name = "autorise_yn")
    private Integer autoriseYN;

    @Column(name = "ordre_inscription")
    private Integer ordreInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    private TypeAdmission typeAdmission;

    @ManyToOne(fetch = FetchType.LAZY)
    private AnneeAcademique anneeAcademique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "region", "typeSelection", "lycee", "informationPersonnelle", "baccalaureat" }, allowSetters = true)
    private Etudiant etudiant;

    @JsonIgnoreProperties(value = { "inscriptionAdministrative" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "inscriptionAdministrative")
    private ProcessusDinscriptionAdministrative processusDinscriptionAdministrative;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InscriptionAdministrative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNouveauInscritYN() {
        return this.nouveauInscritYN;
    }

    public InscriptionAdministrative nouveauInscritYN(Integer nouveauInscritYN) {
        this.setNouveauInscritYN(nouveauInscritYN);
        return this;
    }

    public void setNouveauInscritYN(Integer nouveauInscritYN) {
        this.nouveauInscritYN = nouveauInscritYN;
    }

    public Integer getRepriseYN() {
        return this.repriseYN;
    }

    public InscriptionAdministrative repriseYN(Integer repriseYN) {
        this.setRepriseYN(repriseYN);
        return this;
    }

    public void setRepriseYN(Integer repriseYN) {
        this.repriseYN = repriseYN;
    }

    public Integer getAutoriseYN() {
        return this.autoriseYN;
    }

    public InscriptionAdministrative autoriseYN(Integer autoriseYN) {
        this.setAutoriseYN(autoriseYN);
        return this;
    }

    public void setAutoriseYN(Integer autoriseYN) {
        this.autoriseYN = autoriseYN;
    }

    public Integer getOrdreInscription() {
        return this.ordreInscription;
    }

    public InscriptionAdministrative ordreInscription(Integer ordreInscription) {
        this.setOrdreInscription(ordreInscription);
        return this;
    }

    public void setOrdreInscription(Integer ordreInscription) {
        this.ordreInscription = ordreInscription;
    }

    public TypeAdmission getTypeAdmission() {
        return this.typeAdmission;
    }

    public void setTypeAdmission(TypeAdmission typeAdmission) {
        this.typeAdmission = typeAdmission;
    }

    public InscriptionAdministrative typeAdmission(TypeAdmission typeAdmission) {
        this.setTypeAdmission(typeAdmission);
        return this;
    }

    public AnneeAcademique getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public InscriptionAdministrative anneeAcademique(AnneeAcademique anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public InscriptionAdministrative etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public ProcessusDinscriptionAdministrative getProcessusDinscriptionAdministrative() {
        return this.processusDinscriptionAdministrative;
    }

    public void setProcessusDinscriptionAdministrative(ProcessusDinscriptionAdministrative processusDinscriptionAdministrative) {
        if (this.processusDinscriptionAdministrative != null) {
            this.processusDinscriptionAdministrative.setInscriptionAdministrative(null);
        }
        if (processusDinscriptionAdministrative != null) {
            processusDinscriptionAdministrative.setInscriptionAdministrative(this);
        }
        this.processusDinscriptionAdministrative = processusDinscriptionAdministrative;
    }

    public InscriptionAdministrative processusDinscriptionAdministrative(
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative
    ) {
        this.setProcessusDinscriptionAdministrative(processusDinscriptionAdministrative);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionAdministrative)) {
            return false;
        }
        return getId() != null && getId().equals(((InscriptionAdministrative) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionAdministrative{" +
            "id=" + getId() +
            ", nouveauInscritYN=" + getNouveauInscritYN() +
            ", repriseYN=" + getRepriseYN() +
            ", autoriseYN=" + getAutoriseYN() +
            ", ordreInscription=" + getOrdreInscription() +
            "}";
    }
}
