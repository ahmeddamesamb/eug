package sn.ugb.grh.service.dto;

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
    private String nom;

    @NotNull
    private String prenom;

    private String email;

    private String telephone;

    private Long titresId;

    private String adresse;

    private Integer numeroPoste;

    private String photo;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getTitresId() {
        return titresId;
    }

    public void setTitresId(Long titresId) {
        this.titresId = titresId;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", titresId=" + getTitresId() +
            ", adresse='" + getAdresse() + "'" +
            ", numeroPoste=" + getNumeroPoste() +
            ", photo='" + getPhoto() + "'" +
            "}";
    }
}
