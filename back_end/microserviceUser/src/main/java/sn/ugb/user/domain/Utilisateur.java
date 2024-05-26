package sn.ugb.user.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_user")
    private String nomUser;

    @Column(name = "prenom_user")
    private String prenomUser;

    @Column(name = "email_user")
    private String emailUser;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "role")
    private String role;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "departement")
    private String departement;

    @Column(name = "statut")
    private String statut;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUser() {
        return this.nomUser;
    }

    public Utilisateur nomUser(String nomUser) {
        this.setNomUser(nomUser);
        return this;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return this.prenomUser;
    }

    public Utilisateur prenomUser(String prenomUser) {
        this.setPrenomUser(prenomUser);
        return this;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getEmailUser() {
        return this.emailUser;
    }

    public Utilisateur emailUser(String emailUser) {
        this.setEmailUser(emailUser);
        return this;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Utilisateur motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return this.role;
    }

    public Utilisateur role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Utilisateur matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getDepartement() {
        return this.departement;
    }

    public Utilisateur departement(String departement) {
        this.setDepartement(departement);
        return this;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getStatut() {
        return this.statut;
    }

    public Utilisateur statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", nomUser='" + getNomUser() + "'" +
            ", prenomUser='" + getPrenomUser() + "'" +
            ", emailUser='" + getEmailUser() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", role='" + getRole() + "'" +
            ", matricule='" + getMatricule() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
