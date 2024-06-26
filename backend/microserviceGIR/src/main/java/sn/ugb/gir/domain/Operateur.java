package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operateur.
 */
@Entity
@Table(name = "operateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "operateur")
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
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleOperateur;

    @NotNull
    @Column(name = "user_login", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String userLogin;

    @NotNull
    @Column(name = "code_operateur", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codeOperateur;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "frais", "operateur", "inscriptionAdministrativeFormation" }, allowSetters = true)
    private Set<PaiementFrais> paiementFrais = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "inscriptionAdministrativeFormation", "operateur" }, allowSetters = true)
    private Set<PaiementFormationPrivee> paiementFormationPrivees = new HashSet<>();

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

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Operateur actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<PaiementFrais> getPaiementFrais() {
        return this.paiementFrais;
    }

    public void setPaiementFrais(Set<PaiementFrais> paiementFrais) {
        if (this.paiementFrais != null) {
            this.paiementFrais.forEach(i -> i.setOperateur(null));
        }
        if (paiementFrais != null) {
            paiementFrais.forEach(i -> i.setOperateur(this));
        }
        this.paiementFrais = paiementFrais;
    }

    public Operateur paiementFrais(Set<PaiementFrais> paiementFrais) {
        this.setPaiementFrais(paiementFrais);
        return this;
    }

    public Operateur addPaiementFrais(PaiementFrais paiementFrais) {
        this.paiementFrais.add(paiementFrais);
        paiementFrais.setOperateur(this);
        return this;
    }

    public Operateur removePaiementFrais(PaiementFrais paiementFrais) {
        this.paiementFrais.remove(paiementFrais);
        paiementFrais.setOperateur(null);
        return this;
    }

    public Set<PaiementFormationPrivee> getPaiementFormationPrivees() {
        return this.paiementFormationPrivees;
    }

    public void setPaiementFormationPrivees(Set<PaiementFormationPrivee> paiementFormationPrivees) {
        if (this.paiementFormationPrivees != null) {
            this.paiementFormationPrivees.forEach(i -> i.setOperateur(null));
        }
        if (paiementFormationPrivees != null) {
            paiementFormationPrivees.forEach(i -> i.setOperateur(this));
        }
        this.paiementFormationPrivees = paiementFormationPrivees;
    }

    public Operateur paiementFormationPrivees(Set<PaiementFormationPrivee> paiementFormationPrivees) {
        this.setPaiementFormationPrivees(paiementFormationPrivees);
        return this;
    }

    public Operateur addPaiementFormationPrivees(PaiementFormationPrivee paiementFormationPrivee) {
        this.paiementFormationPrivees.add(paiementFormationPrivee);
        paiementFormationPrivee.setOperateur(this);
        return this;
    }

    public Operateur removePaiementFormationPrivees(PaiementFormationPrivee paiementFormationPrivee) {
        this.paiementFormationPrivees.remove(paiementFormationPrivee);
        paiementFormationPrivee.setOperateur(null);
        return this;
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
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
