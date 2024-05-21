package sn.ugb.grh.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enseignant.
 */
@Entity
@Table(name = "enseignant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titre_co_encadreur")
    private String titreCoEncadreur;

    @NotNull
    @Column(name = "nom_enseignant", nullable = false)
    private String nomEnseignant;

    @NotNull
    @Column(name = "prenom_enseignant", nullable = false)
    private String prenomEnseignant;

    @Column(name = "email_enseignant")
    private String emailEnseignant;

    @Column(name = "telephone_enseignant")
    private String telephoneEnseignant;

    @Column(name = "titres_id")
    private Integer titresId;

    @Column(name = "utilisateurs_id")
    private Integer utilisateursId;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "numero_poste")
    private Integer numeroPoste;

    @Lob
    @Column(name = "photo_enseignant")
    private byte[] photoEnseignant;

    @Column(name = "photo_enseignant_content_type")
    private String photoEnseignantContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enseignant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreCoEncadreur() {
        return this.titreCoEncadreur;
    }

    public Enseignant titreCoEncadreur(String titreCoEncadreur) {
        this.setTitreCoEncadreur(titreCoEncadreur);
        return this;
    }

    public void setTitreCoEncadreur(String titreCoEncadreur) {
        this.titreCoEncadreur = titreCoEncadreur;
    }

    public String getNomEnseignant() {
        return this.nomEnseignant;
    }

    public Enseignant nomEnseignant(String nomEnseignant) {
        this.setNomEnseignant(nomEnseignant);
        return this;
    }

    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    public String getPrenomEnseignant() {
        return this.prenomEnseignant;
    }

    public Enseignant prenomEnseignant(String prenomEnseignant) {
        this.setPrenomEnseignant(prenomEnseignant);
        return this;
    }

    public void setPrenomEnseignant(String prenomEnseignant) {
        this.prenomEnseignant = prenomEnseignant;
    }

    public String getEmailEnseignant() {
        return this.emailEnseignant;
    }

    public Enseignant emailEnseignant(String emailEnseignant) {
        this.setEmailEnseignant(emailEnseignant);
        return this;
    }

    public void setEmailEnseignant(String emailEnseignant) {
        this.emailEnseignant = emailEnseignant;
    }

    public String getTelephoneEnseignant() {
        return this.telephoneEnseignant;
    }

    public Enseignant telephoneEnseignant(String telephoneEnseignant) {
        this.setTelephoneEnseignant(telephoneEnseignant);
        return this;
    }

    public void setTelephoneEnseignant(String telephoneEnseignant) {
        this.telephoneEnseignant = telephoneEnseignant;
    }

    public Integer getTitresId() {
        return this.titresId;
    }

    public Enseignant titresId(Integer titresId) {
        this.setTitresId(titresId);
        return this;
    }

    public void setTitresId(Integer titresId) {
        this.titresId = titresId;
    }

    public Integer getUtilisateursId() {
        return this.utilisateursId;
    }

    public Enseignant utilisateursId(Integer utilisateursId) {
        this.setUtilisateursId(utilisateursId);
        return this;
    }

    public void setUtilisateursId(Integer utilisateursId) {
        this.utilisateursId = utilisateursId;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Enseignant adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getNumeroPoste() {
        return this.numeroPoste;
    }

    public Enseignant numeroPoste(Integer numeroPoste) {
        this.setNumeroPoste(numeroPoste);
        return this;
    }

    public void setNumeroPoste(Integer numeroPoste) {
        this.numeroPoste = numeroPoste;
    }

    public byte[] getPhotoEnseignant() {
        return this.photoEnseignant;
    }

    public Enseignant photoEnseignant(byte[] photoEnseignant) {
        this.setPhotoEnseignant(photoEnseignant);
        return this;
    }

    public void setPhotoEnseignant(byte[] photoEnseignant) {
        this.photoEnseignant = photoEnseignant;
    }

    public String getPhotoEnseignantContentType() {
        return this.photoEnseignantContentType;
    }

    public Enseignant photoEnseignantContentType(String photoEnseignantContentType) {
        this.photoEnseignantContentType = photoEnseignantContentType;
        return this;
    }

    public void setPhotoEnseignantContentType(String photoEnseignantContentType) {
        this.photoEnseignantContentType = photoEnseignantContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enseignant)) {
            return false;
        }
        return getId() != null && getId().equals(((Enseignant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enseignant{" +
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
            ", photoEnseignantContentType='" + getPhotoEnseignantContentType() + "'" +
            "}";
    }
}
