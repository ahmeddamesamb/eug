package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.ProcessusInscriptionAdministrative} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessusInscriptionAdministrativeDTO implements Serializable {

    private Long id;

    private Boolean inscriptionDemarreeYN;

    private Instant dateDemarrageInscription;

    private Boolean inscriptionAnnuleeYN;

    private Instant dateAnnulationInscription;

    private Boolean apteYN;

    private Instant dateVisiteMedicale;

    private Boolean beneficiaireCROUSYN;

    private Instant dateRemiseQuitusCROUS;

    private Boolean nouveauCodeBUYN;

    private Boolean quitusBUYN;

    private Instant dateRemiseQuitusBU;

    private Boolean exporterBUYN;

    private Boolean fraisObligatoiresPayesYN;

    private Instant datePaiementFraisObligatoires;

    private Integer numeroQuitusCROUS;

    private Boolean carteEturemiseYN;

    private Boolean carteDupremiseYN;

    private Instant dateRemiseCarteEtu;

    private Integer dateRemiseCarteDup;

    private Boolean inscritAdministrativementYN;

    private Instant dateInscriptionAdministrative;

    private Instant derniereModification;

    private Boolean inscritOnlineYN;

    private String emailUser;

    private InscriptionAdministrativeDTO inscriptionAdministrative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInscriptionDemarreeYN() {
        return inscriptionDemarreeYN;
    }

    public void setInscriptionDemarreeYN(Boolean inscriptionDemarreeYN) {
        this.inscriptionDemarreeYN = inscriptionDemarreeYN;
    }

    public Instant getDateDemarrageInscription() {
        return dateDemarrageInscription;
    }

    public void setDateDemarrageInscription(Instant dateDemarrageInscription) {
        this.dateDemarrageInscription = dateDemarrageInscription;
    }

    public Boolean getInscriptionAnnuleeYN() {
        return inscriptionAnnuleeYN;
    }

    public void setInscriptionAnnuleeYN(Boolean inscriptionAnnuleeYN) {
        this.inscriptionAnnuleeYN = inscriptionAnnuleeYN;
    }

    public Instant getDateAnnulationInscription() {
        return dateAnnulationInscription;
    }

    public void setDateAnnulationInscription(Instant dateAnnulationInscription) {
        this.dateAnnulationInscription = dateAnnulationInscription;
    }

    public Boolean getApteYN() {
        return apteYN;
    }

    public void setApteYN(Boolean apteYN) {
        this.apteYN = apteYN;
    }

    public Instant getDateVisiteMedicale() {
        return dateVisiteMedicale;
    }

    public void setDateVisiteMedicale(Instant dateVisiteMedicale) {
        this.dateVisiteMedicale = dateVisiteMedicale;
    }

    public Boolean getBeneficiaireCROUSYN() {
        return beneficiaireCROUSYN;
    }

    public void setBeneficiaireCROUSYN(Boolean beneficiaireCROUSYN) {
        this.beneficiaireCROUSYN = beneficiaireCROUSYN;
    }

    public Instant getDateRemiseQuitusCROUS() {
        return dateRemiseQuitusCROUS;
    }

    public void setDateRemiseQuitusCROUS(Instant dateRemiseQuitusCROUS) {
        this.dateRemiseQuitusCROUS = dateRemiseQuitusCROUS;
    }

    public Boolean getNouveauCodeBUYN() {
        return nouveauCodeBUYN;
    }

    public void setNouveauCodeBUYN(Boolean nouveauCodeBUYN) {
        this.nouveauCodeBUYN = nouveauCodeBUYN;
    }

    public Boolean getQuitusBUYN() {
        return quitusBUYN;
    }

    public void setQuitusBUYN(Boolean quitusBUYN) {
        this.quitusBUYN = quitusBUYN;
    }

    public Instant getDateRemiseQuitusBU() {
        return dateRemiseQuitusBU;
    }

    public void setDateRemiseQuitusBU(Instant dateRemiseQuitusBU) {
        this.dateRemiseQuitusBU = dateRemiseQuitusBU;
    }

    public Boolean getExporterBUYN() {
        return exporterBUYN;
    }

    public void setExporterBUYN(Boolean exporterBUYN) {
        this.exporterBUYN = exporterBUYN;
    }

    public Boolean getFraisObligatoiresPayesYN() {
        return fraisObligatoiresPayesYN;
    }

    public void setFraisObligatoiresPayesYN(Boolean fraisObligatoiresPayesYN) {
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

    public Boolean getCarteEturemiseYN() {
        return carteEturemiseYN;
    }

    public void setCarteEturemiseYN(Boolean carteEturemiseYN) {
        this.carteEturemiseYN = carteEturemiseYN;
    }

    public Boolean getCarteDupremiseYN() {
        return carteDupremiseYN;
    }

    public void setCarteDupremiseYN(Boolean carteDupremiseYN) {
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

    public Boolean getInscritAdministrativementYN() {
        return inscritAdministrativementYN;
    }

    public void setInscritAdministrativementYN(Boolean inscritAdministrativementYN) {
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

    public Boolean getInscritOnlineYN() {
        return inscritOnlineYN;
    }

    public void setInscritOnlineYN(Boolean inscritOnlineYN) {
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
        if (!(o instanceof ProcessusInscriptionAdministrativeDTO)) {
            return false;
        }

        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = (ProcessusInscriptionAdministrativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processusInscriptionAdministrativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessusInscriptionAdministrativeDTO{" +
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
            ", inscriptionAdministrative=" + getInscriptionAdministrative() +
            "}";
    }
}
