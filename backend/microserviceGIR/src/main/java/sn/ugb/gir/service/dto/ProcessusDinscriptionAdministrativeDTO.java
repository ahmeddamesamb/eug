package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.ProcessusDinscriptionAdministrative} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessusDinscriptionAdministrativeDTO implements Serializable {

    private Long id;

    private Integer inscriptionDemarreeYN;

    private Instant dateDemarrageInscription;

    private Integer inscriptionAnnuleeYN;

    private Instant dateAnnulationInscription;

    private Integer apteYN;

    private Instant dateVisiteMedicale;

    private Integer beneficiaireCROUSYN;

    private Instant dateRemiseQuitusCROUS;

    private Integer nouveauCodeBUYN;

    private Integer quitusBUYN;

    private Instant dateRemiseQuitusBU;

    private Integer exporterBUYN;

    private Integer fraisObligatoiresPayesYN;

    private Instant datePaiementFraisObligatoires;

    private Integer numeroQuitusCROUS;

    private Integer carteEturemiseYN;

    private Integer carteDupremiseYN;

    private Instant dateRemiseCarteEtu;

    private Integer dateRemiseCarteDup;

    private Integer inscritAdministrativementYN;

    private Instant dateInscriptionAdministrative;

    private Instant derniereModification;

    private Integer inscritOnlineYN;

    private String emailUser;

    private InscriptionAdministrativeDTO inscriptionAdministrative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInscriptionDemarreeYN() {
        return inscriptionDemarreeYN;
    }

    public void setInscriptionDemarreeYN(Integer inscriptionDemarreeYN) {
        this.inscriptionDemarreeYN = inscriptionDemarreeYN;
    }

    public Instant getDateDemarrageInscription() {
        return dateDemarrageInscription;
    }

    public void setDateDemarrageInscription(Instant dateDemarrageInscription) {
        this.dateDemarrageInscription = dateDemarrageInscription;
    }

    public Integer getInscriptionAnnuleeYN() {
        return inscriptionAnnuleeYN;
    }

    public void setInscriptionAnnuleeYN(Integer inscriptionAnnuleeYN) {
        this.inscriptionAnnuleeYN = inscriptionAnnuleeYN;
    }

    public Instant getDateAnnulationInscription() {
        return dateAnnulationInscription;
    }

    public void setDateAnnulationInscription(Instant dateAnnulationInscription) {
        this.dateAnnulationInscription = dateAnnulationInscription;
    }

    public Integer getApteYN() {
        return apteYN;
    }

    public void setApteYN(Integer apteYN) {
        this.apteYN = apteYN;
    }

    public Instant getDateVisiteMedicale() {
        return dateVisiteMedicale;
    }

    public void setDateVisiteMedicale(Instant dateVisiteMedicale) {
        this.dateVisiteMedicale = dateVisiteMedicale;
    }

    public Integer getBeneficiaireCROUSYN() {
        return beneficiaireCROUSYN;
    }

    public void setBeneficiaireCROUSYN(Integer beneficiaireCROUSYN) {
        this.beneficiaireCROUSYN = beneficiaireCROUSYN;
    }

    public Instant getDateRemiseQuitusCROUS() {
        return dateRemiseQuitusCROUS;
    }

    public void setDateRemiseQuitusCROUS(Instant dateRemiseQuitusCROUS) {
        this.dateRemiseQuitusCROUS = dateRemiseQuitusCROUS;
    }

    public Integer getNouveauCodeBUYN() {
        return nouveauCodeBUYN;
    }

    public void setNouveauCodeBUYN(Integer nouveauCodeBUYN) {
        this.nouveauCodeBUYN = nouveauCodeBUYN;
    }

    public Integer getQuitusBUYN() {
        return quitusBUYN;
    }

    public void setQuitusBUYN(Integer quitusBUYN) {
        this.quitusBUYN = quitusBUYN;
    }

    public Instant getDateRemiseQuitusBU() {
        return dateRemiseQuitusBU;
    }

    public void setDateRemiseQuitusBU(Instant dateRemiseQuitusBU) {
        this.dateRemiseQuitusBU = dateRemiseQuitusBU;
    }

    public Integer getExporterBUYN() {
        return exporterBUYN;
    }

    public void setExporterBUYN(Integer exporterBUYN) {
        this.exporterBUYN = exporterBUYN;
    }

    public Integer getFraisObligatoiresPayesYN() {
        return fraisObligatoiresPayesYN;
    }

    public void setFraisObligatoiresPayesYN(Integer fraisObligatoiresPayesYN) {
        this.fraisObligatoiresPayesYN = fraisObligatoiresPayesYN;
    }

    public Instant getDatePaiementFraisObligatoires() {
        return datePaiementFraisObligatoires;
    }

    public void setDatePaiementFraisObligatoires(Instant datePaiementFraisObligatoires) {
        this.datePaiementFraisObligatoires = datePaiementFraisObligatoires;
    }

    public Integer getNumeroQuitusCROUS() {
        return numeroQuitusCROUS;
    }

    public void setNumeroQuitusCROUS(Integer numeroQuitusCROUS) {
        this.numeroQuitusCROUS = numeroQuitusCROUS;
    }

    public Integer getCarteEturemiseYN() {
        return carteEturemiseYN;
    }

    public void setCarteEturemiseYN(Integer carteEturemiseYN) {
        this.carteEturemiseYN = carteEturemiseYN;
    }

    public Integer getCarteDupremiseYN() {
        return carteDupremiseYN;
    }

    public void setCarteDupremiseYN(Integer carteDupremiseYN) {
        this.carteDupremiseYN = carteDupremiseYN;
    }

    public Instant getDateRemiseCarteEtu() {
        return dateRemiseCarteEtu;
    }

    public void setDateRemiseCarteEtu(Instant dateRemiseCarteEtu) {
        this.dateRemiseCarteEtu = dateRemiseCarteEtu;
    }

    public Integer getDateRemiseCarteDup() {
        return dateRemiseCarteDup;
    }

    public void setDateRemiseCarteDup(Integer dateRemiseCarteDup) {
        this.dateRemiseCarteDup = dateRemiseCarteDup;
    }

    public Integer getInscritAdministrativementYN() {
        return inscritAdministrativementYN;
    }

    public void setInscritAdministrativementYN(Integer inscritAdministrativementYN) {
        this.inscritAdministrativementYN = inscritAdministrativementYN;
    }

    public Instant getDateInscriptionAdministrative() {
        return dateInscriptionAdministrative;
    }

    public void setDateInscriptionAdministrative(Instant dateInscriptionAdministrative) {
        this.dateInscriptionAdministrative = dateInscriptionAdministrative;
    }

    public Instant getDerniereModification() {
        return derniereModification;
    }

    public void setDerniereModification(Instant derniereModification) {
        this.derniereModification = derniereModification;
    }

    public Integer getInscritOnlineYN() {
        return inscritOnlineYN;
    }

    public void setInscritOnlineYN(Integer inscritOnlineYN) {
        this.inscritOnlineYN = inscritOnlineYN;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public InscriptionAdministrativeDTO getInscriptionAdministrative() {
        return inscriptionAdministrative;
    }

    public void setInscriptionAdministrative(InscriptionAdministrativeDTO inscriptionAdministrative) {
        this.inscriptionAdministrative = inscriptionAdministrative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessusDinscriptionAdministrativeDTO)) {
            return false;
        }

        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = (ProcessusDinscriptionAdministrativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processusDinscriptionAdministrativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessusDinscriptionAdministrativeDTO{" +
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
            ", inscriptionAdministrative=" + getInscriptionAdministrative() +
            "}";
    }
}
