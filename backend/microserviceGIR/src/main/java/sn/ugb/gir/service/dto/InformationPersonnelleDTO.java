package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.InformationPersonnelle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationPersonnelleDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomEtu;

    private String nomJeuneFilleEtu;

    @NotNull
    private String prenomEtu;

    @NotNull
    private String statutMarital;

    private Integer regime;

    private String profession;

    @NotNull
    private String adresseEtu;

    private String telEtu;

    private String emailEtu;

    private String adresseParent;

    private String telParent;

    private String emailParent;

    private String nomParent;

    private String prenomParent;

    private Integer handicapYN;

    private Integer ordiPersoYN;

    private LocalDate derniereModification;

    private String emailUser;

    private EtudiantDTO etudiant;

    private TypeHandicapDTO typeHadique;

    private TypeBourseDTO typeBourse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEtu() {
        return nomEtu;
    }

    public void setNomEtu(String nomEtu) {
        this.nomEtu = nomEtu;
    }

    public String getNomJeuneFilleEtu() {
        return nomJeuneFilleEtu;
    }

    public void setNomJeuneFilleEtu(String nomJeuneFilleEtu) {
        this.nomJeuneFilleEtu = nomJeuneFilleEtu;
    }

    public String getPrenomEtu() {
        return prenomEtu;
    }

    public void setPrenomEtu(String prenomEtu) {
        this.prenomEtu = prenomEtu;
    }

    public String getStatutMarital() {
        return statutMarital;
    }

    public void setStatutMarital(String statutMarital) {
        this.statutMarital = statutMarital;
    }

    public Integer getRegime() {
        return regime;
    }

    public void setRegime(Integer regime) {
        this.regime = regime;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAdresseEtu() {
        return adresseEtu;
    }

    public void setAdresseEtu(String adresseEtu) {
        this.adresseEtu = adresseEtu;
    }

    public String getTelEtu() {
        return telEtu;
    }

    public void setTelEtu(String telEtu) {
        this.telEtu = telEtu;
    }

    public String getEmailEtu() {
        return emailEtu;
    }

    public void setEmailEtu(String emailEtu) {
        this.emailEtu = emailEtu;
    }

    public String getAdresseParent() {
        return adresseParent;
    }

    public void setAdresseParent(String adresseParent) {
        this.adresseParent = adresseParent;
    }

    public String getTelParent() {
        return telParent;
    }

    public void setTelParent(String telParent) {
        this.telParent = telParent;
    }

    public String getEmailParent() {
        return emailParent;
    }

    public void setEmailParent(String emailParent) {
        this.emailParent = emailParent;
    }

    public String getNomParent() {
        return nomParent;
    }

    public void setNomParent(String nomParent) {
        this.nomParent = nomParent;
    }

    public String getPrenomParent() {
        return prenomParent;
    }

    public void setPrenomParent(String prenomParent) {
        this.prenomParent = prenomParent;
    }

    public Integer getHandicapYN() {
        return handicapYN;
    }

    public void setHandicapYN(Integer handicapYN) {
        this.handicapYN = handicapYN;
    }

    public Integer getOrdiPersoYN() {
        return ordiPersoYN;
    }

    public void setOrdiPersoYN(Integer ordiPersoYN) {
        this.ordiPersoYN = ordiPersoYN;
    }

    public LocalDate getDerniereModification() {
        return derniereModification;
    }

    public void setDerniereModification(LocalDate derniereModification) {
        this.derniereModification = derniereModification;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    public TypeHandicapDTO getTypeHadique() {
        return typeHadique;
    }

    public void setTypeHadique(TypeHandicapDTO typeHadique) {
        this.typeHadique = typeHadique;
    }

    public TypeBourseDTO getTypeBourse() {
        return typeBourse;
    }

    public void setTypeBourse(TypeBourseDTO typeBourse) {
        this.typeBourse = typeBourse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InformationPersonnelleDTO)) {
            return false;
        }

        InformationPersonnelleDTO informationPersonnelleDTO = (InformationPersonnelleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, informationPersonnelleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformationPersonnelleDTO{" +
            "id=" + getId() +
            ", nomEtu='" + getNomEtu() + "'" +
            ", nomJeuneFilleEtu='" + getNomJeuneFilleEtu() + "'" +
            ", prenomEtu='" + getPrenomEtu() + "'" +
            ", statutMarital='" + getStatutMarital() + "'" +
            ", regime=" + getRegime() +
            ", profession='" + getProfession() + "'" +
            ", adresseEtu='" + getAdresseEtu() + "'" +
            ", telEtu='" + getTelEtu() + "'" +
            ", emailEtu='" + getEmailEtu() + "'" +
            ", adresseParent='" + getAdresseParent() + "'" +
            ", telParent='" + getTelParent() + "'" +
            ", emailParent='" + getEmailParent() + "'" +
            ", nomParent='" + getNomParent() + "'" +
            ", prenomParent='" + getPrenomParent() + "'" +
            ", handicapYN=" + getHandicapYN() +
            ", ordiPersoYN=" + getOrdiPersoYN() +
            ", derniereModification='" + getDerniereModification() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            ", etudiant=" + getEtudiant() +
            ", typeHadique=" + getTypeHadique() +
            ", typeBourse=" + getTypeBourse() +
            "}";
    }
}
