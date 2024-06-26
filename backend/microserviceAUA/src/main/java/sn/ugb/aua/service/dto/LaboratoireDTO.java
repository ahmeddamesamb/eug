package sn.ugb.aua.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.aua.domain.Laboratoire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LaboratoireDTO implements Serializable {

    private Long id;

    private String nom;

    private Boolean laboratoireCotutelleYN;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Boolean getLaboratoireCotutelleYN() {
        return laboratoireCotutelleYN;
    }

    public void setLaboratoireCotutelleYN(Boolean laboratoireCotutelleYN) {
        this.laboratoireCotutelleYN = laboratoireCotutelleYN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LaboratoireDTO)) {
            return false;
        }

        LaboratoireDTO laboratoireDTO = (LaboratoireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, laboratoireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LaboratoireDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", laboratoireCotutelleYN='" + getLaboratoireCotutelleYN() + "'" +
            "}";
    }
}
