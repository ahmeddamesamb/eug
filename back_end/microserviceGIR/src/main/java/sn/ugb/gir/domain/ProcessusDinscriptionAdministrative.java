package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProcessusDinscriptionAdministrative.
 */
@Entity
@Table(name = "processus_dinscription_administrative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessusDinscriptionAdministrative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "inscription_demarree_yn")
    private Integer inscriptionDemarreeYN;

    @Column(name = "date_demarrage_inscription")
    private Instant dateDemarrageInscription;

    @Column(name = "inscription_annulee_yn")
    private Integer inscriptionAnnuleeYN;

    @Column(name = "date_annulation_inscription")
    private Instant dateAnnulationInscription;

    @Column(name = "apte_yn")
    private Integer apteYN;

    @Column(name = "date_visite_medicale")
    private Instant dateVisiteMedicale;

    @Column(name = "beneficiaire_crousyn")
    private Integer beneficiaireCROUSYN;

    @Column(name = "date_remise_quitus_crous")
    private Instant dateRemiseQuitusCROUS;

    @Column(name = "nouveau_code_buyn")
    private Integer nouveauCodeBUYN;

    @Column(name = "quitus_buyn")
    private Integer quitusBUYN;

    @Column(name = "date_remise_quitus_bu")
    private Instant dateRemiseQuitusBU;

    @Column(name = "exporter_buyn")
    private Integer exporterBUYN;

    @Column(name = "frais_obligatoires_payes_yn")
    private Integer fraisObligatoiresPayesYN;

    @Column(name = "date_paiement_frais_obligatoires")
    private Instant datePaiementFraisObligatoires;

    @Column(name = "numero_quitus_crous")
    private Integer numeroQuitusCROUS;

    @Column(name = "carte_eturemise_yn")
    private Integer carteEturemiseYN;

    @Column(name = "carte_dupremise_yn")
    private Integer carteDupremiseYN;

    @Column(name = "date_remise_carte_etu")
    private Instant dateRemiseCarteEtu;

    @Column(name = "date_remise_carte_dup")
    private Integer dateRemiseCarteDup;

    @Column(name = "inscrit_administrativement_yn")
    private Integer inscritAdministrativementYN;

    @Column(name = "date_inscription_administrative")
    private Instant dateInscriptionAdministrative;

    @Column(name = "derniere_modification")
    private Instant derniereModification;

    @Column(name = "inscrit_online_yn")
    private Integer inscritOnlineYN;

    @Column(name = "email_user")
    private String emailUser;

    @JsonIgnoreProperties(
        value = { "typeAdmission", "anneeAcademique", "etudiant", "processusDinscriptionAdministrative" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private InscriptionAdministrative inscriptionAdministrative;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProcessusDinscriptionAdministrative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInscriptionDemarreeYN() {
        return this.inscriptionDemarreeYN;
    }

    public ProcessusDinscriptionAdministrative inscriptionDemarreeYN(Integer inscriptionDemarreeYN) {
        this.setInscriptionDemarreeYN(inscriptionDemarreeYN);
        return this;
    }

    public void setInscriptionDemarreeYN(Integer inscriptionDemarreeYN) {
        this.inscriptionDemarreeYN = inscriptionDemarreeYN;
    }

    public Instant getDateDemarrageInscription() {
        return this.dateDemarrageInscription;
    }

    public ProcessusDinscriptionAdministrative dateDemarrageInscription(Instant dateDemarrageInscription) {
        this.setDateDemarrageInscription(dateDemarrageInscription);
        return this;
    }

    public void setDateDemarrageInscription(Instant dateDemarrageInscription) {
        this.dateDemarrageInscription = dateDemarrageInscription;
    }

    public Integer getInscriptionAnnuleeYN() {
        return this.inscriptionAnnuleeYN;
    }

    public ProcessusDinscriptionAdministrative inscriptionAnnuleeYN(Integer inscriptionAnnuleeYN) {
        this.setInscriptionAnnuleeYN(inscriptionAnnuleeYN);
        return this;
    }

    public void setInscriptionAnnuleeYN(Integer inscriptionAnnuleeYN) {
        this.inscriptionAnnuleeYN = inscriptionAnnuleeYN;
    }

    public Instant getDateAnnulationInscription() {
        return this.dateAnnulationInscription;
    }

    public ProcessusDinscriptionAdministrative dateAnnulationInscription(Instant dateAnnulationInscription) {
        this.setDateAnnulationInscription(dateAnnulationInscription);
        return this;
    }

    public void setDateAnnulationInscription(Instant dateAnnulationInscription) {
        this.dateAnnulationInscription = dateAnnulationInscription;
    }

    public Integer getApteYN() {
        return this.apteYN;
    }

    public ProcessusDinscriptionAdministrative apteYN(Integer apteYN) {
        this.setApteYN(apteYN);
        return this;
    }

    public void setApteYN(Integer apteYN) {
        this.apteYN = apteYN;
    }

    public Instant getDateVisiteMedicale() {
        return this.dateVisiteMedicale;
    }

    public ProcessusDinscriptionAdministrative dateVisiteMedicale(Instant dateVisiteMedicale) {
        this.setDateVisiteMedicale(dateVisiteMedicale);
        return this;
    }

    public void setDateVisiteMedicale(Instant dateVisiteMedicale) {
        this.dateVisiteMedicale = dateVisiteMedicale;
    }

    public Integer getBeneficiaireCROUSYN() {
        return this.beneficiaireCROUSYN;
    }

    public ProcessusDinscriptionAdministrative beneficiaireCROUSYN(Integer beneficiaireCROUSYN) {
        this.setBeneficiaireCROUSYN(beneficiaireCROUSYN);
        return this;
    }

    public void setBeneficiaireCROUSYN(Integer beneficiaireCROUSYN) {
        this.beneficiaireCROUSYN = beneficiaireCROUSYN;
    }

    public Instant getDateRemiseQuitusCROUS() {
        return this.dateRemiseQuitusCROUS;
    }

    public ProcessusDinscriptionAdministrative dateRemiseQuitusCROUS(Instant dateRemiseQuitusCROUS) {
        this.setDateRemiseQuitusCROUS(dateRemiseQuitusCROUS);
        return this;
    }

    public void setDateRemiseQuitusCROUS(Instant dateRemiseQuitusCROUS) {
        this.dateRemiseQuitusCROUS = dateRemiseQuitusCROUS;
    }

    public Integer getNouveauCodeBUYN() {
        return this.nouveauCodeBUYN;
    }

    public ProcessusDinscriptionAdministrative nouveauCodeBUYN(Integer nouveauCodeBUYN) {
        this.setNouveauCodeBUYN(nouveauCodeBUYN);
        return this;
    }

    public void setNouveauCodeBUYN(Integer nouveauCodeBUYN) {
        this.nouveauCodeBUYN = nouveauCodeBUYN;
    }

    public Integer getQuitusBUYN() {
        return this.quitusBUYN;
    }

    public ProcessusDinscriptionAdministrative quitusBUYN(Integer quitusBUYN) {
        this.setQuitusBUYN(quitusBUYN);
        return this;
    }

    public void setQuitusBUYN(Integer quitusBUYN) {
        this.quitusBUYN = quitusBUYN;
    }

    public Instant getDateRemiseQuitusBU() {
        return this.dateRemiseQuitusBU;
    }

    public ProcessusDinscriptionAdministrative dateRemiseQuitusBU(Instant dateRemiseQuitusBU) {
        this.setDateRemiseQuitusBU(dateRemiseQuitusBU);
        return this;
    }

    public void setDateRemiseQuitusBU(Instant dateRemiseQuitusBU) {
        this.dateRemiseQuitusBU = dateRemiseQuitusBU;
    }

    public Integer getExporterBUYN() {
        return this.exporterBUYN;
    }

    public ProcessusDinscriptionAdministrative exporterBUYN(Integer exporterBUYN) {
        this.setExporterBUYN(exporterBUYN);
        return this;
    }

    public void setExporterBUYN(Integer exporterBUYN) {
        this.exporterBUYN = exporterBUYN;
    }

    public Integer getFraisObligatoiresPayesYN() {
        return this.fraisObligatoiresPayesYN;
    }

    public ProcessusDinscriptionAdministrative fraisObligatoiresPayesYN(Integer fraisObligatoiresPayesYN) {
        this.setFraisObligatoiresPayesYN(fraisObligatoiresPayesYN);
        return this;
    }

    public void setFraisObligatoiresPayesYN(Integer fraisObligatoiresPayesYN) {
        this.fraisObligatoiresPayesYN = fraisObligatoiresPayesYN;
    }

    public Instant getDatePaiementFraisObligatoires() {
        return this.datePaiementFraisObligatoires;
    }

    public ProcessusDinscriptionAdministrative datePaiementFraisObligatoires(Instant datePaiementFraisObligatoires) {
        this.setDatePaiementFraisObligatoires(datePaiementFraisObligatoires);
        return this;
    }

    public void setDatePaiementFraisObligatoires(Instant datePaiementFraisObligatoires) {
        this.datePaiementFraisObligatoires = datePaiementFraisObligatoires;
    }

    public Integer getNumeroQuitusCROUS() {
        return this.numeroQuitusCROUS;
    }

    public ProcessusDinscriptionAdministrative numeroQuitusCROUS(Integer numeroQuitusCROUS) {
        this.setNumeroQuitusCROUS(numeroQuitusCROUS);
        return this;
    }

    public void setNumeroQuitusCROUS(Integer numeroQuitusCROUS) {
        this.numeroQuitusCROUS = numeroQuitusCROUS;
    }

    public Integer getCarteEturemiseYN() {
        return this.carteEturemiseYN;
    }

    public ProcessusDinscriptionAdministrative carteEturemiseYN(Integer carteEturemiseYN) {
        this.setCarteEturemiseYN(carteEturemiseYN);
        return this;
    }

    public void setCarteEturemiseYN(Integer carteEturemiseYN) {
        this.carteEturemiseYN = carteEturemiseYN;
    }

    public Integer getCarteDupremiseYN() {
        return this.carteDupremiseYN;
    }

    public ProcessusDinscriptionAdministrative carteDupremiseYN(Integer carteDupremiseYN) {
        this.setCarteDupremiseYN(carteDupremiseYN);
        return this;
    }

    public void setCarteDupremiseYN(Integer carteDupremiseYN) {
        this.carteDupremiseYN = carteDupremiseYN;
    }

    public Instant getDateRemiseCarteEtu() {
        return this.dateRemiseCarteEtu;
    }

    public ProcessusDinscriptionAdministrative dateRemiseCarteEtu(Instant dateRemiseCarteEtu) {
        this.setDateRemiseCarteEtu(dateRemiseCarteEtu);
        return this;
    }

    public void setDateRemiseCarteEtu(Instant dateRemiseCarteEtu) {
        this.dateRemiseCarteEtu = dateRemiseCarteEtu;
    }

    public Integer getDateRemiseCarteDup() {
        return this.dateRemiseCarteDup;
    }

    public ProcessusDinscriptionAdministrative dateRemiseCarteDup(Integer dateRemiseCarteDup) {
        this.setDateRemiseCarteDup(dateRemiseCarteDup);
        return this;
    }

    public void setDateRemiseCarteDup(Integer dateRemiseCarteDup) {
        this.dateRemiseCarteDup = dateRemiseCarteDup;
    }

    public Integer getInscritAdministrativementYN() {
        return this.inscritAdministrativementYN;
    }

    public ProcessusDinscriptionAdministrative inscritAdministrativementYN(Integer inscritAdministrativementYN) {
        this.setInscritAdministrativementYN(inscritAdministrativementYN);
        return this;
    }

    public void setInscritAdministrativementYN(Integer inscritAdministrativementYN) {
        this.inscritAdministrativementYN = inscritAdministrativementYN;
    }

    public Instant getDateInscriptionAdministrative() {
        return this.dateInscriptionAdministrative;
    }

    public ProcessusDinscriptionAdministrative dateInscriptionAdministrative(Instant dateInscriptionAdministrative) {
        this.setDateInscriptionAdministrative(dateInscriptionAdministrative);
        return this;
    }

    public void setDateInscriptionAdministrative(Instant dateInscriptionAdministrative) {
        this.dateInscriptionAdministrative = dateInscriptionAdministrative;
    }

    public Instant getDerniereModification() {
        return this.derniereModification;
    }

    public ProcessusDinscriptionAdministrative derniereModification(Instant derniereModification) {
        this.setDerniereModification(derniereModification);
        return this;
    }

    public void setDerniereModification(Instant derniereModification) {
        this.derniereModification = derniereModification;
    }

    public Integer getInscritOnlineYN() {
        return this.inscritOnlineYN;
    }

    public ProcessusDinscriptionAdministrative inscritOnlineYN(Integer inscritOnlineYN) {
        this.setInscritOnlineYN(inscritOnlineYN);
        return this;
    }

    public void setInscritOnlineYN(Integer inscritOnlineYN) {
        this.inscritOnlineYN = inscritOnlineYN;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public ProcessusDinscriptionAdministrative emailUser(String emailUser) {
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

    public ProcessusDinscriptionAdministrative inscriptionAdministrative(InscriptionAdministrative inscriptionAdministrative) {
        this.setInscriptionAdministrative(inscriptionAdministrative);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessusDinscriptionAdministrative)) {
            return false;
        }
        return getId() != null && getId().equals(((ProcessusDinscriptionAdministrative) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessusDinscriptionAdministrative{" +
            "id=" + getId() +
            ", inscriptionDemarreeYN=" + getInscriptionDemarreeYN() +
            ", dateDemarrageInscription='" + getDateDemarrageInscription() + "'" +
            ", inscriptionAnnuleeYN=" + getInscriptionAnnuleeYN() +
            ", dateAnnulationInscription='" + getDateAnnulationInscription() + "'" +
            ", apteYN=" + getApteYN() +
            ", dateVisiteMedicale='" + getDateVisiteMedicale() + "'" +
            ", beneficiaireCROUSYN=" + getBeneficiaireCROUSYN() +
            ", dateRemiseQuitusCROUS='" + getDateRemiseQuitusCROUS() + "'" +
            ", nouveauCodeBUYN=" + getNouveauCodeBUYN() +
            ", quitusBUYN=" + getQuitusBUYN() +
            ", dateRemiseQuitusBU='" + getDateRemiseQuitusBU() + "'" +
            ", exporterBUYN=" + getExporterBUYN() +
            ", fraisObligatoiresPayesYN=" + getFraisObligatoiresPayesYN() +
            ", datePaiementFraisObligatoires='" + getDatePaiementFraisObligatoires() + "'" +
            ", numeroQuitusCROUS=" + getNumeroQuitusCROUS() +
            ", carteEturemiseYN=" + getCarteEturemiseYN() +
            ", carteDupremiseYN=" + getCarteDupremiseYN() +
            ", dateRemiseCarteEtu='" + getDateRemiseCarteEtu() + "'" +
            ", dateRemiseCarteDup=" + getDateRemiseCarteDup() +
            ", inscritAdministrativementYN=" + getInscritAdministrativementYN() +
            ", dateInscriptionAdministrative='" + getDateInscriptionAdministrative() + "'" +
            ", derniereModification='" + getDerniereModification() + "'" +
            ", inscritOnlineYN=" + getInscritOnlineYN() +
            ", emailUser='" + getEmailUser() + "'" +
            "}";
    }
}
