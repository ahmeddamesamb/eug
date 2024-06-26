package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProcessusInscriptionAdministrative.
 */
@Entity
@Table(name = "processus_inscription_administrative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "processusinscriptionadministrative")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessusInscriptionAdministrative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "inscription_demarree_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean inscriptionDemarreeYN;

    @Column(name = "date_demarrage_inscription")
    private Instant dateDemarrageInscription;

    @Column(name = "inscription_annulee_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean inscriptionAnnuleeYN;

    @Column(name = "date_annulation_inscription")
    private Instant dateAnnulationInscription;

    @Column(name = "apte_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean apteYN;

    @Column(name = "date_visite_medicale")
    private Instant dateVisiteMedicale;

    @Column(name = "beneficiaire_crousyn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean beneficiaireCROUSYN;

    @Column(name = "date_remise_quitus_crous")
    private Instant dateRemiseQuitusCROUS;

    @Column(name = "nouveau_code_buyn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean nouveauCodeBUYN;

    @Column(name = "quitus_buyn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean quitusBUYN;

    @Column(name = "date_remise_quitus_bu")
    private Instant dateRemiseQuitusBU;

    @Column(name = "exporter_buyn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean exporterBUYN;

    @Column(name = "frais_obligatoires_payes_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean fraisObligatoiresPayesYN;

    @Column(name = "date_paiement_frais_obligatoires")
    private Instant datePaiementFraisObligatoires;

    @Column(name = "numero_quitus_crous")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numeroQuitusCROUS;

    @Column(name = "carte_eturemise_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean carteEturemiseYN;

    @Column(name = "carte_dupremise_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean carteDupremiseYN;

    @Column(name = "date_remise_carte_etu")
    private Instant dateRemiseCarteEtu;

    @Column(name = "date_remise_carte_dup")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dateRemiseCarteDup;

    @Column(name = "inscrit_administrativement_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean inscritAdministrativementYN;

    @Column(name = "date_inscription_administrative")
    private Instant dateInscriptionAdministrative;

    @Column(name = "derniere_modification")
    private Instant derniereModification;

    @Column(name = "inscrit_online_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean inscritOnlineYN;

    @Column(name = "email_user")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String emailUser;

    @JsonIgnoreProperties(
        value = {
            "typeAdmission", "anneeAcademique", "etudiant", "processusDinscriptionAdministrative", "inscriptionAdministrativeFormations",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private InscriptionAdministrative inscriptionAdministrative;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProcessusInscriptionAdministrative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInscriptionDemarreeYN() {
        return this.inscriptionDemarreeYN;
    }

    public ProcessusInscriptionAdministrative inscriptionDemarreeYN(Boolean inscriptionDemarreeYN) {
        this.setInscriptionDemarreeYN(inscriptionDemarreeYN);
        return this;
    }

    public void setInscriptionDemarreeYN(Boolean inscriptionDemarreeYN) {
        this.inscriptionDemarreeYN = inscriptionDemarreeYN;
    }

    public Instant getDateDemarrageInscription() {
        return this.dateDemarrageInscription;
    }

    public ProcessusInscriptionAdministrative dateDemarrageInscription(Instant dateDemarrageInscription) {
        this.setDateDemarrageInscription(dateDemarrageInscription);
        return this;
    }

    public void setDateDemarrageInscription(Instant dateDemarrageInscription) {
        this.dateDemarrageInscription = dateDemarrageInscription;
    }

    public Boolean getInscriptionAnnuleeYN() {
        return this.inscriptionAnnuleeYN;
    }

    public ProcessusInscriptionAdministrative inscriptionAnnuleeYN(Boolean inscriptionAnnuleeYN) {
        this.setInscriptionAnnuleeYN(inscriptionAnnuleeYN);
        return this;
    }

    public void setInscriptionAnnuleeYN(Boolean inscriptionAnnuleeYN) {
        this.inscriptionAnnuleeYN = inscriptionAnnuleeYN;
    }

    public Instant getDateAnnulationInscription() {
        return this.dateAnnulationInscription;
    }

    public ProcessusInscriptionAdministrative dateAnnulationInscription(Instant dateAnnulationInscription) {
        this.setDateAnnulationInscription(dateAnnulationInscription);
        return this;
    }

    public void setDateAnnulationInscription(Instant dateAnnulationInscription) {
        this.dateAnnulationInscription = dateAnnulationInscription;
    }

    public Boolean getApteYN() {
        return this.apteYN;
    }

    public ProcessusInscriptionAdministrative apteYN(Boolean apteYN) {
        this.setApteYN(apteYN);
        return this;
    }

    public void setApteYN(Boolean apteYN) {
        this.apteYN = apteYN;
    }

    public Instant getDateVisiteMedicale() {
        return this.dateVisiteMedicale;
    }

    public ProcessusInscriptionAdministrative dateVisiteMedicale(Instant dateVisiteMedicale) {
        this.setDateVisiteMedicale(dateVisiteMedicale);
        return this;
    }

    public void setDateVisiteMedicale(Instant dateVisiteMedicale) {
        this.dateVisiteMedicale = dateVisiteMedicale;
    }

    public Boolean getBeneficiaireCROUSYN() {
        return this.beneficiaireCROUSYN;
    }

    public ProcessusInscriptionAdministrative beneficiaireCROUSYN(Boolean beneficiaireCROUSYN) {
        this.setBeneficiaireCROUSYN(beneficiaireCROUSYN);
        return this;
    }

    public void setBeneficiaireCROUSYN(Boolean beneficiaireCROUSYN) {
        this.beneficiaireCROUSYN = beneficiaireCROUSYN;
    }

    public Instant getDateRemiseQuitusCROUS() {
        return this.dateRemiseQuitusCROUS;
    }

    public ProcessusInscriptionAdministrative dateRemiseQuitusCROUS(Instant dateRemiseQuitusCROUS) {
        this.setDateRemiseQuitusCROUS(dateRemiseQuitusCROUS);
        return this;
    }

    public void setDateRemiseQuitusCROUS(Instant dateRemiseQuitusCROUS) {
        this.dateRemiseQuitusCROUS = dateRemiseQuitusCROUS;
    }

    public Boolean getNouveauCodeBUYN() {
        return this.nouveauCodeBUYN;
    }

    public ProcessusInscriptionAdministrative nouveauCodeBUYN(Boolean nouveauCodeBUYN) {
        this.setNouveauCodeBUYN(nouveauCodeBUYN);
        return this;
    }

    public void setNouveauCodeBUYN(Boolean nouveauCodeBUYN) {
        this.nouveauCodeBUYN = nouveauCodeBUYN;
    }

    public Boolean getQuitusBUYN() {
        return this.quitusBUYN;
    }

    public ProcessusInscriptionAdministrative quitusBUYN(Boolean quitusBUYN) {
        this.setQuitusBUYN(quitusBUYN);
        return this;
    }

    public void setQuitusBUYN(Boolean quitusBUYN) {
        this.quitusBUYN = quitusBUYN;
    }

    public Instant getDateRemiseQuitusBU() {
        return this.dateRemiseQuitusBU;
    }

    public ProcessusInscriptionAdministrative dateRemiseQuitusBU(Instant dateRemiseQuitusBU) {
        this.setDateRemiseQuitusBU(dateRemiseQuitusBU);
        return this;
    }

    public void setDateRemiseQuitusBU(Instant dateRemiseQuitusBU) {
        this.dateRemiseQuitusBU = dateRemiseQuitusBU;
    }

    public Boolean getExporterBUYN() {
        return this.exporterBUYN;
    }

    public ProcessusInscriptionAdministrative exporterBUYN(Boolean exporterBUYN) {
        this.setExporterBUYN(exporterBUYN);
        return this;
    }

    public void setExporterBUYN(Boolean exporterBUYN) {
        this.exporterBUYN = exporterBUYN;
    }

    public Boolean getFraisObligatoiresPayesYN() {
        return this.fraisObligatoiresPayesYN;
    }

    public ProcessusInscriptionAdministrative fraisObligatoiresPayesYN(Boolean fraisObligatoiresPayesYN) {
        this.setFraisObligatoiresPayesYN(fraisObligatoiresPayesYN);
        return this;
    }

    public void setFraisObligatoiresPayesYN(Boolean fraisObligatoiresPayesYN) {
        this.fraisObligatoiresPayesYN = fraisObligatoiresPayesYN;
    }

    public Instant getDatePaiementFraisObligatoires() {
        return this.datePaiementFraisObligatoires;
    }

    public ProcessusInscriptionAdministrative datePaiementFraisObligatoires(Instant datePaiementFraisObligatoires) {
        this.setDatePaiementFraisObligatoires(datePaiementFraisObligatoires);
        return this;
    }

    public void setDatePaiementFraisObligatoires(Instant datePaiementFraisObligatoires) {
        this.datePaiementFraisObligatoires = datePaiementFraisObligatoires;
    }

    public Integer getNumeroQuitusCROUS() {
        return this.numeroQuitusCROUS;
    }

    public ProcessusInscriptionAdministrative numeroQuitusCROUS(Integer numeroQuitusCROUS) {
        this.setNumeroQuitusCROUS(numeroQuitusCROUS);
        return this;
    }

    public void setNumeroQuitusCROUS(Integer numeroQuitusCROUS) {
        this.numeroQuitusCROUS = numeroQuitusCROUS;
    }

    public Boolean getCarteEturemiseYN() {
        return this.carteEturemiseYN;
    }

    public ProcessusInscriptionAdministrative carteEturemiseYN(Boolean carteEturemiseYN) {
        this.setCarteEturemiseYN(carteEturemiseYN);
        return this;
    }

    public void setCarteEturemiseYN(Boolean carteEturemiseYN) {
        this.carteEturemiseYN = carteEturemiseYN;
    }

    public Boolean getCarteDupremiseYN() {
        return this.carteDupremiseYN;
    }

    public ProcessusInscriptionAdministrative carteDupremiseYN(Boolean carteDupremiseYN) {
        this.setCarteDupremiseYN(carteDupremiseYN);
        return this;
    }

    public void setCarteDupremiseYN(Boolean carteDupremiseYN) {
        this.carteDupremiseYN = carteDupremiseYN;
    }

    public Instant getDateRemiseCarteEtu() {
        return this.dateRemiseCarteEtu;
    }

    public ProcessusInscriptionAdministrative dateRemiseCarteEtu(Instant dateRemiseCarteEtu) {
        this.setDateRemiseCarteEtu(dateRemiseCarteEtu);
        return this;
    }

    public void setDateRemiseCarteEtu(Instant dateRemiseCarteEtu) {
        this.dateRemiseCarteEtu = dateRemiseCarteEtu;
    }

    public Integer getDateRemiseCarteDup() {
        return this.dateRemiseCarteDup;
    }

    public ProcessusInscriptionAdministrative dateRemiseCarteDup(Integer dateRemiseCarteDup) {
        this.setDateRemiseCarteDup(dateRemiseCarteDup);
        return this;
    }

    public void setDateRemiseCarteDup(Integer dateRemiseCarteDup) {
        this.dateRemiseCarteDup = dateRemiseCarteDup;
    }

    public Boolean getInscritAdministrativementYN() {
        return this.inscritAdministrativementYN;
    }

    public ProcessusInscriptionAdministrative inscritAdministrativementYN(Boolean inscritAdministrativementYN) {
        this.setInscritAdministrativementYN(inscritAdministrativementYN);
        return this;
    }

    public void setInscritAdministrativementYN(Boolean inscritAdministrativementYN) {
        this.inscritAdministrativementYN = inscritAdministrativementYN;
    }

    public Instant getDateInscriptionAdministrative() {
        return this.dateInscriptionAdministrative;
    }

    public ProcessusInscriptionAdministrative dateInscriptionAdministrative(Instant dateInscriptionAdministrative) {
        this.setDateInscriptionAdministrative(dateInscriptionAdministrative);
        return this;
    }

    public void setDateInscriptionAdministrative(Instant dateInscriptionAdministrative) {
        this.dateInscriptionAdministrative = dateInscriptionAdministrative;
    }

    public Instant getDerniereModification() {
        return this.derniereModification;
    }

    public ProcessusInscriptionAdministrative derniereModification(Instant derniereModification) {
        this.setDerniereModification(derniereModification);
        return this;
    }

    public void setDerniereModification(Instant derniereModification) {
        this.derniereModification = derniereModification;
    }

    public Boolean getInscritOnlineYN() {
        return this.inscritOnlineYN;
    }

    public ProcessusInscriptionAdministrative inscritOnlineYN(Boolean inscritOnlineYN) {
        this.setInscritOnlineYN(inscritOnlineYN);
        return this;
    }

    public void setInscritOnlineYN(Boolean inscritOnlineYN) {
        this.inscritOnlineYN = inscritOnlineYN;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public ProcessusInscriptionAdministrative emailUser(String emailUser) {
        this.setEmailUser(emailUser);
        return this;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public InscriptionAdministrative getInscriptionAdministrative() {
        return this.inscriptionAdministrative;
    }

    public void setInscriptionAdministrative(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministrative = inscriptionAdministrative;
    }

    public ProcessusInscriptionAdministrative inscriptionAdministrative(InscriptionAdministrative inscriptionAdministrative) {
        this.setInscriptionAdministrative(inscriptionAdministrative);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessusInscriptionAdministrative)) {
            return false;
        }
        return getId() != null && getId().equals(((ProcessusInscriptionAdministrative) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessusInscriptionAdministrative{" +
            "id=" + getId() +
            ", inscriptionDemarreeYN='" + getInscriptionDemarreeYN() + "'" +
            ", dateDemarrageInscription='" + getDateDemarrageInscription() + "'" +
            ", inscriptionAnnuleeYN='" + getInscriptionAnnuleeYN() + "'" +
            ", dateAnnulationInscription='" + getDateAnnulationInscription() + "'" +
            ", apteYN='" + getApteYN() + "'" +
            ", dateVisiteMedicale='" + getDateVisiteMedicale() + "'" +
            ", beneficiaireCROUSYN='" + getBeneficiaireCROUSYN() + "'" +
            ", dateRemiseQuitusCROUS='" + getDateRemiseQuitusCROUS() + "'" +
            ", nouveauCodeBUYN='" + getNouveauCodeBUYN() + "'" +
            ", quitusBUYN='" + getQuitusBUYN() + "'" +
            ", dateRemiseQuitusBU='" + getDateRemiseQuitusBU() + "'" +
            ", exporterBUYN='" + getExporterBUYN() + "'" +
            ", fraisObligatoiresPayesYN='" + getFraisObligatoiresPayesYN() + "'" +
            ", datePaiementFraisObligatoires='" + getDatePaiementFraisObligatoires() + "'" +
            ", numeroQuitusCROUS=" + getNumeroQuitusCROUS() +
            ", carteEturemiseYN='" + getCarteEturemiseYN() + "'" +
            ", carteDupremiseYN='" + getCarteDupremiseYN() + "'" +
            ", dateRemiseCarteEtu='" + getDateRemiseCarteEtu() + "'" +
            ", dateRemiseCarteDup=" + getDateRemiseCarteDup() +
            ", inscritAdministrativementYN='" + getInscritAdministrativementYN() + "'" +
            ", dateInscriptionAdministrative='" + getDateInscriptionAdministrative() + "'" +
            ", derniereModification='" + getDerniereModification() + "'" +
            ", inscritOnlineYN='" + getInscritOnlineYN() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            "}";
    }
}
