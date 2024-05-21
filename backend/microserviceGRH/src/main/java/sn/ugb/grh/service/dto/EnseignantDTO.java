package sn.ugb.grh.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.grh.domain.Enseignant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnseignantDTO implements Serializable {

    private Long id;

    private String titreCoEncadreur;

    @NotNull
    private String nomEnseignant;

    @NotNull
    private String prenomEnseignant;

    private String emailEnseignant;

    private String telephoneEnseignant;

    private Integer titresId;

    private Integer utilisateursId;

    private String adresse;

    private Integer numeroPoste;

    @Lob
    private byte[] photoEnseignant;

    private String photoEnseignantContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreCoEncadreur() {
        return titreCoEncadreur;
    }

    public void setTitreCoEncadreur(String titreCoEncadreur) {
        this.titreCoEncadreur = titreCoEncadreur;
    }

    public String getNomEnseignant() {
        return nomEnseignant;
    }

    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    public String getPrenomEnseignant() {
        return prenomEnseignant;
    }

    public void setPrenomEnseignant(String prenomEnseignant) {
        this.prenomEnseignant = prenomEnseignant;
    }

    public String getEmailEnseignant() {
        return emailEnseignant;
    }

    public void setEmailEnseignant(String emailEnseignant) {
        this.emailEnseignant = emailEnseignant;
    }

    public String getTelephoneEnseignant() {
        return telephoneEnseignant;
    }

    public void setTelephoneEnseignant(String telephoneEnseignant) {
        this.telephoneEnseignant = telephoneEnseignant;
    }

    public Integer getTitresId() {
        return titresId;
    }

    public void setTitresId(Integer titresId) {
        this.titresId = titresId;
    }

    public Integer getUtilisateursId() {
        return utilisateursId;
    }

    public void setUtilisateursId(Integer utilisateursId) {
        this.utilisateursId = utilisateursId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getNumeroPoste() {
        return numeroPoste;
    }

    public void setNumeroPoste(Integer numeroPoste) {
        this.numeroPoste = numeroPoste;
    }

    public byte[] getPhotoEnseignant() {
        return photoEnseignant;
    }

    public void setPhotoEnseignant(byte[] photoEnseignant) {
        this.photoEnseignant = photoEnseignant;
    }

    public String getPhotoEnseignantContentType() {
        return photoEnseignantContentType;
    }

    public void setPhotoEnseignantContentType(String photoEnseignantContentType) {
        this.photoEnseignantContentType = photoEnseignantContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnseignantDTO)) {
            return false;
        }

        EnseignantDTO enseignantDTO = (EnseignantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enseignantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnseignantDTO{" +
            "id=" + getId() +
            ", titreCoEncadreur='" + getTitreCoEncadreur() + "'" +
            ", nomEnseignant='" + getNomEnseignant() + "'" +
            ", prenomEnseignant='" + getPrenomEnseignant() + "'" +
            ", emailEnseignant='" + getEmailEnseignant() + "'" +
            ", telephoneEnseignant='" + getTelephoneEnseignant() + "'" +
            ", titresId=" + getTitresId() +
            ", utilisateursId=" + getUtilisateursId() +
            ", adresse='" + getAdresse() + "'" +
            ", numeroPoste=" + getNumeroPoste() +
            ", photoEnseignant='" + getPhotoEnseignant() + "'" +
            "}";
    }
}
