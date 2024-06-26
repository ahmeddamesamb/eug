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
 * A InfosUser.
 */
@Table("infos_user")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "infosuser")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfosUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("date_ajout")
    private LocalDate dateAjout;

    @NotNull(message = "must not be null")
    @Column("actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @Transient
    private User user;

    @Transient
    @JsonIgnoreProperties(value = { "infoUser" }, allowSetters = true)
    private Set<HistoriqueConnexion> historiqueConnexions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "profil", "infoUser", "userProfileBlocFonctionnels" }, allowSetters = true)
    private Set<UserProfile> userProfiles = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "infosUser", "ressource" }, allowSetters = true)
    private Set<InfoUserRessource> infoUserRessources = new HashSet<>();

    @Column("user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfosUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAjout() {
        return this.dateAjout;
    }

    public InfosUser dateAjout(LocalDate dateAjout) {
        this.setDateAjout(dateAjout);
        return this;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public InfosUser actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public InfosUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<HistoriqueConnexion> getHistoriqueConnexions() {
        return this.historiqueConnexions;
    }

    public void setHistoriqueConnexions(Set<HistoriqueConnexion> historiqueConnexions) {
        if (this.historiqueConnexions != null) {
            this.historiqueConnexions.forEach(i -> i.setInfoUser(null));
        }
        if (historiqueConnexions != null) {
            historiqueConnexions.forEach(i -> i.setInfoUser(this));
        }
        this.historiqueConnexions = historiqueConnexions;
    }

    public InfosUser historiqueConnexions(Set<HistoriqueConnexion> historiqueConnexions) {
        this.setHistoriqueConnexions(historiqueConnexions);
        return this;
    }

    public InfosUser addHistoriqueConnexions(HistoriqueConnexion historiqueConnexion) {
        this.historiqueConnexions.add(historiqueConnexion);
        historiqueConnexion.setInfoUser(this);
        return this;
    }

    public InfosUser removeHistoriqueConnexions(HistoriqueConnexion historiqueConnexion) {
        this.historiqueConnexions.remove(historiqueConnexion);
        historiqueConnexion.setInfoUser(null);
        return this;
    }

    public Set<UserProfile> getUserProfiles() {
        return this.userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        if (this.userProfiles != null) {
            this.userProfiles.forEach(i -> i.setInfoUser(null));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.setInfoUser(this));
        }
        this.userProfiles = userProfiles;
    }

    public InfosUser userProfiles(Set<UserProfile> userProfiles) {
        this.setUserProfiles(userProfiles);
        return this;
    }

    public InfosUser addUserProfiles(UserProfile userProfile) {
        this.userProfiles.add(userProfile);
        userProfile.setInfoUser(this);
        return this;
    }

    public InfosUser removeUserProfiles(UserProfile userProfile) {
        this.userProfiles.remove(userProfile);
        userProfile.setInfoUser(null);
        return this;
    }

    public Set<InfoUserRessource> getInfoUserRessources() {
        return this.infoUserRessources;
    }

    public void setInfoUserRessources(Set<InfoUserRessource> infoUserRessources) {
        if (this.infoUserRessources != null) {
            this.infoUserRessources.forEach(i -> i.setInfosUser(null));
        }
        if (infoUserRessources != null) {
            infoUserRessources.forEach(i -> i.setInfosUser(this));
        }
        this.infoUserRessources = infoUserRessources;
    }

    public InfosUser infoUserRessources(Set<InfoUserRessource> infoUserRessources) {
        this.setInfoUserRessources(infoUserRessources);
        return this;
    }

    public InfosUser addInfoUserRessources(InfoUserRessource infoUserRessource) {
        this.infoUserRessources.add(infoUserRessource);
        infoUserRessource.setInfosUser(this);
        return this;
    }

    public InfosUser removeInfoUserRessources(InfoUserRessource infoUserRessource) {
        this.infoUserRessources.remove(infoUserRessource);
        infoUserRessource.setInfosUser(null);
        return this;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfosUser)) {
            return false;
        }
        return getId() != null && getId().equals(((InfosUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfosUser{" +
            "id=" + getId() +
            ", dateAjout='" + getDateAjout() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
