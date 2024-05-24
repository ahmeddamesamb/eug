package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operateur.
 */
@Entity
@Table(name = "operateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Operateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_operateur", nullable = false, unique = true)
    private String libelleOperateur;

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @NotNull
    @Column(name = "code_operateur", nullable = false, unique = true)
    private String codeOperateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleOperateur() {
        return this.libelleOperateur;
    }

    public Operateur libelleOperateur(String libelleOperateur) {
        this.setLibelleOperateur(libelleOperateur);
        return this;
    }

    public void setLibelleOperateur(String libelleOperateur) {
        this.libelleOperateur = libelleOperateur;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public Operateur userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getCodeOperateur() {
        return this.codeOperateur;
    }

    public Operateur codeOperateur(String codeOperateur) {
        this.setCodeOperateur(codeOperateur);
        return this;
    }

    public void setCodeOperateur(String codeOperateur) {
        this.codeOperateur = codeOperateur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Operateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operateur{" +
            "id=" + getId() +
            ", libelleOperateur='" + getLibelleOperateur() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", codeOperateur='" + getCodeOperateur() + "'" +
            "}";
    }
}
