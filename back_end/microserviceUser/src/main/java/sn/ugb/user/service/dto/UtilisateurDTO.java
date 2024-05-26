package sn.ugb.user.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.user.domain.Utilisateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurDTO implements Serializable {

    private Long id;

    private String nomUser;

    private String prenomUser;

    private String emailUser;

    private String motDePasse;

    private String role;

    private String matricule;

    private String departement;

    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurDTO)) {
            return false;
        }

        UtilisateurDTO utilisateurDTO = (UtilisateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilisateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurDTO{" +
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
