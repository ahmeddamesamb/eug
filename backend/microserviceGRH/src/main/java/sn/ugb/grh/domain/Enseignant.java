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
@org.springframework.data.elasticsearch.annotations.Document(indexName = "enseignant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titre_co_encadreur")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String titreCoEncadreur;

    @NotNull
    @Column(name = "nom", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String prenom;

    @Column(name = "email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Column(name = "telephone")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String telephone;

    @Column(name = "titres_id")
    private Long titresId;

    @Column(name = "adresse")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String adresse;

    @Column(name = "numero_poste")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numeroPoste;

    @Column(name = "photo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String photo;

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

    public String getNom() {
        return this.nom;
    }

    public Enseignant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Enseignant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Enseignant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Enseignant telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getTitresId() {
        return this.titresId;
    }

    public Enseignant titresId(Long titresId) {
        this.setTitresId(titresId);
        return this;
    }

    public void setTitresId(Long titresId) {
        this.titresId = titresId;
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

    public String getPhoto() {
        return this.photo;
    }

    public Enseignant photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
