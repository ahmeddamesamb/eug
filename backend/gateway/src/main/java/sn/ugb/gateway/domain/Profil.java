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
 * A Profil.
 */
@Table("profil")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "profil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Profil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelle;

    @NotNull(message = "must not be null")
    @Column("date_ajout")
    private LocalDate dateAjout;

    @NotNull(message = "must not be null")
    @Column("actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @Transient
    @JsonIgnoreProperties(value = { "profil", "infoUser", "userProfileBlocFonctionnels" }, allowSetters = true)
    private Set<UserProfile> userProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Profil libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDateAjout() {
        return this.dateAjout;
    }

    public Profil dateAjout(LocalDate dateAjout) {
        this.setDateAjout(dateAjout);
        return this;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Profil actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<UserProfile> getUserProfiles() {
        return this.userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        if (this.userProfiles != null) {
            this.userProfiles.forEach(i -> i.setProfil(null));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.setProfil(this));
        }
        this.userProfiles = userProfiles;
    }

    public Profil userProfiles(Set<UserProfile> userProfiles) {
        this.setUserProfiles(userProfiles);
        return this;
    }

    public Profil addUserProfiles(UserProfile userProfile) {
        this.userProfiles.add(userProfile);
        userProfile.setProfil(this);
        return this;
    }

    public Profil removeUserProfiles(UserProfile userProfile) {
        this.userProfiles.remove(userProfile);
        userProfile.setProfil(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profil)) {
            return false;
        }
        return getId() != null && getId().equals(((Profil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profil{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateAjout='" + getDateAjout() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
