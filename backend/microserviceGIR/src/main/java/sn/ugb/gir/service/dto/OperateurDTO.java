package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Operateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperateurDTO implements Serializable {

    private Long id;

    private String libelle;

    private String userLogin;

    private String codeOperateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getCodeOperateur() {
        return codeOperateur;
    }

    public void setCodeOperateur(String codeOperateur) {
        this.codeOperateur = codeOperateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperateurDTO)) {
            return false;
        }

        OperateurDTO operateurDTO = (OperateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperateurDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", codeOperateur='" + getCodeOperateur() + "'" +
            "}";
    }
}
