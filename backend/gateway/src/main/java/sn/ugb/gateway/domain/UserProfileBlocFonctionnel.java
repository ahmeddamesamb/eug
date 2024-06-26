package sn.ugb.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserProfileBlocFonctionnel.
 */
@Table("user_profile_bloc_fonctionnel")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userprofileblocfonctionnel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileBlocFonctionnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("date")
    private LocalDate date;

    @NotNull(message = "must not be null")
    @Column("en_cours_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean enCoursYN;

    @Transient
    @JsonIgnoreProperties(value = { "profil", "infoUser", "userProfileBlocFonctionnels" }, allowSetters = true)
    private UserProfile userProfil;

    @Transient
    @JsonIgnoreProperties(value = { "service", "userProfileBlocFonctionnels" }, allowSetters = true)
    private BlocFonctionnel blocFonctionnel;

    @Column("user_profil_id")
    private Long userProfilId;

    @Column("bloc_fonctionnel_id")
    private Long blocFonctionnelId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfileBlocFonctionnel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public UserProfileBlocFonctionnel date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getEnCoursYN() {
        return this.enCoursYN;
    }

    public UserProfileBlocFonctionnel enCoursYN(Boolean enCoursYN) {
        this.setEnCoursYN(enCoursYN);
        return this;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public UserProfile getUserProfil() {
        return this.userProfil;
    }

    public void setUserProfil(UserProfile userProfile) {
        this.userProfil = userProfile;
        this.userProfilId = userProfile != null ? userProfile.getId() : null;
    }

    public UserProfileBlocFonctionnel userProfil(UserProfile userProfile) {
        this.setUserProfil(userProfile);
        return this;
    }

    public BlocFonctionnel getBlocFonctionnel() {
        return this.blocFonctionnel;
    }

    public void setBlocFonctionnel(BlocFonctionnel blocFonctionnel) {
        this.blocFonctionnel = blocFonctionnel;
        this.blocFonctionnelId = blocFonctionnel != null ? blocFonctionnel.getId() : null;
    }

    public UserProfileBlocFonctionnel blocFonctionnel(BlocFonctionnel blocFonctionnel) {
        this.setBlocFonctionnel(blocFonctionnel);
        return this;
    }

    public Long getUserProfilId() {
        return this.userProfilId;
    }

    public void setUserProfilId(Long userProfile) {
        this.userProfilId = userProfile;
    }

    public Long getBlocFonctionnelId() {
        return this.blocFonctionnelId;
    }

    public void setBlocFonctionnelId(Long blocFonctionnel) {
        this.blocFonctionnelId = blocFonctionnel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileBlocFonctionnel)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfileBlocFonctionnel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileBlocFonctionnel{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            "}";
    }
}
