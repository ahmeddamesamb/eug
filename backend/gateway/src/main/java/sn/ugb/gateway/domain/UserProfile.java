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
 * A UserProfile.
 */
@Table("user_profile")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userprofile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("date_debut")
    private LocalDate dateDebut;

    @NotNull(message = "must not be null")
    @Column("date_fin")
    private LocalDate dateFin;

    @NotNull(message = "must not be null")
    @Column("en_cours_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean enCoursYN;

    @Transient
    @JsonIgnoreProperties(value = { "userProfiles" }, allowSetters = true)
    private Profil profil;

    @Transient
    @JsonIgnoreProperties(value = { "user", "historiqueConnexions", "userProfiles", "infoUserRessources" }, allowSetters = true)
    private InfosUser infoUser;

    @Transient
    @JsonIgnoreProperties(value = { "userProfil", "blocFonctionnel" }, allowSetters = true)
    private Set<UserProfileBlocFonctionnel> userProfileBlocFonctionnels = new HashSet<>();

    @Column("profil_id")
    private Long profilId;

    @Column("info_user_id")
    private Long infoUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public UserProfile dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public UserProfile dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getEnCoursYN() {
        return this.enCoursYN;
    }

    public UserProfile enCoursYN(Boolean enCoursYN) {
        this.setEnCoursYN(enCoursYN);
        return this;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public Profil getProfil() {
        return this.profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
        this.profilId = profil != null ? profil.getId() : null;
    }

    public UserProfile profil(Profil profil) {
        this.setProfil(profil);
        return this;
    }

    public InfosUser getInfoUser() {
        return this.infoUser;
    }

    public void setInfoUser(InfosUser infosUser) {
        this.infoUser = infosUser;
        this.infoUserId = infosUser != null ? infosUser.getId() : null;
    }

    public UserProfile infoUser(InfosUser infosUser) {
        this.setInfoUser(infosUser);
        return this;
    }

    public Set<UserProfileBlocFonctionnel> getUserProfileBlocFonctionnels() {
        return this.userProfileBlocFonctionnels;
    }

    public void setUserProfileBlocFonctionnels(Set<UserProfileBlocFonctionnel> userProfileBlocFonctionnels) {
        if (this.userProfileBlocFonctionnels != null) {
            this.userProfileBlocFonctionnels.forEach(i -> i.setUserProfil(null));
        }
        if (userProfileBlocFonctionnels != null) {
            userProfileBlocFonctionnels.forEach(i -> i.setUserProfil(this));
        }
        this.userProfileBlocFonctionnels = userProfileBlocFonctionnels;
    }

    public UserProfile userProfileBlocFonctionnels(Set<UserProfileBlocFonctionnel> userProfileBlocFonctionnels) {
        this.setUserProfileBlocFonctionnels(userProfileBlocFonctionnels);
        return this;
    }

    public UserProfile addUserProfileBlocFonctionnels(UserProfileBlocFonctionnel userProfileBlocFonctionnel) {
        this.userProfileBlocFonctionnels.add(userProfileBlocFonctionnel);
        userProfileBlocFonctionnel.setUserProfil(this);
        return this;
    }

    public UserProfile removeUserProfileBlocFonctionnels(UserProfileBlocFonctionnel userProfileBlocFonctionnel) {
        this.userProfileBlocFonctionnels.remove(userProfileBlocFonctionnel);
        userProfileBlocFonctionnel.setUserProfil(null);
        return this;
    }

    public Long getProfilId() {
        return this.profilId;
    }

    public void setProfilId(Long profil) {
        this.profilId = profil;
    }

    public Long getInfoUserId() {
        return this.infoUserId;
    }

    public void setInfoUserId(Long infosUser) {
        this.infoUserId = infosUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            "}";
    }
}
