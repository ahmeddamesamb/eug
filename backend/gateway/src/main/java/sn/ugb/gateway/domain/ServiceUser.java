package sn.ugb.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ServiceUser.
 */
@Table("service_user")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "serviceuser")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nom;

    @NotNull(message = "must not be null")
    @Column("date_ajout")
    private LocalDate dateAjout;

    @NotNull(message = "must not be null")
    @Column("actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @Transient
    @JsonIgnoreProperties(value = { "service", "userProfileBlocFonctionnels" }, allowSetters = true)
    private Set<BlocFonctionnel> blocFonctionnels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public ServiceUser nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateAjout() {
        return this.dateAjout;
    }

    public ServiceUser dateAjout(LocalDate dateAjout) {
        this.setDateAjout(dateAjout);
        return this;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public ServiceUser actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<BlocFonctionnel> getBlocFonctionnels() {
        return this.blocFonctionnels;
    }

    public void setBlocFonctionnels(Set<BlocFonctionnel> blocFonctionnels) {
        if (this.blocFonctionnels != null) {
            this.blocFonctionnels.forEach(i -> i.setService(null));
        }
        if (blocFonctionnels != null) {
            blocFonctionnels.forEach(i -> i.setService(this));
        }
        this.blocFonctionnels = blocFonctionnels;
    }

    public ServiceUser blocFonctionnels(Set<BlocFonctionnel> blocFonctionnels) {
        this.setBlocFonctionnels(blocFonctionnels);
        return this;
    }

    public ServiceUser addBlocFonctionnels(BlocFonctionnel blocFonctionnel) {
        this.blocFonctionnels.add(blocFonctionnel);
        blocFonctionnel.setService(this);
        return this;
    }

    public ServiceUser removeBlocFonctionnels(BlocFonctionnel blocFonctionnel) {
        this.blocFonctionnels.remove(blocFonctionnel);
        blocFonctionnel.setService(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceUser{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateAjout='" + getDateAjout() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
