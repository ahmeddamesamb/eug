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
 * A BlocFonctionnel.
 */
@Table("bloc_fonctionnel")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "blocfonctionnel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlocFonctionnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle_bloc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleBloc;

    @NotNull(message = "must not be null")
    @Column("date_ajout_bloc")
    private LocalDate dateAjoutBloc;

    @NotNull(message = "must not be null")
    @Column("actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @Transient
    @JsonIgnoreProperties(value = { "blocFonctionnels" }, allowSetters = true)
    private ServiceUser service;

    @Transient
    @JsonIgnoreProperties(value = { "userProfil", "blocFonctionnel" }, allowSetters = true)
    private Set<UserProfileBlocFonctionnel> userProfileBlocFonctionnels = new HashSet<>();

    @Column("service_id")
    private Long serviceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BlocFonctionnel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleBloc() {
        return this.libelleBloc;
    }

    public BlocFonctionnel libelleBloc(String libelleBloc) {
        this.setLibelleBloc(libelleBloc);
        return this;
    }

    public void setLibelleBloc(String libelleBloc) {
        this.libelleBloc = libelleBloc;
    }

    public LocalDate getDateAjoutBloc() {
        return this.dateAjoutBloc;
    }

    public BlocFonctionnel dateAjoutBloc(LocalDate dateAjoutBloc) {
        this.setDateAjoutBloc(dateAjoutBloc);
        return this;
    }

    public void setDateAjoutBloc(LocalDate dateAjoutBloc) {
        this.dateAjoutBloc = dateAjoutBloc;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public BlocFonctionnel actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public ServiceUser getService() {
        return this.service;
    }

    public void setService(ServiceUser serviceUser) {
        this.service = serviceUser;
        this.serviceId = serviceUser != null ? serviceUser.getId() : null;
    }

    public BlocFonctionnel service(ServiceUser serviceUser) {
        this.setService(serviceUser);
        return this;
    }

    public Set<UserProfileBlocFonctionnel> getUserProfileBlocFonctionnels() {
        return this.userProfileBlocFonctionnels;
    }

    public void setUserProfileBlocFonctionnels(Set<UserProfileBlocFonctionnel> userProfileBlocFonctionnels) {
        if (this.userProfileBlocFonctionnels != null) {
            this.userProfileBlocFonctionnels.forEach(i -> i.setBlocFonctionnel(null));
        }
        if (userProfileBlocFonctionnels != null) {
            userProfileBlocFonctionnels.forEach(i -> i.setBlocFonctionnel(this));
        }
        this.userProfileBlocFonctionnels = userProfileBlocFonctionnels;
    }

    public BlocFonctionnel userProfileBlocFonctionnels(Set<UserProfileBlocFonctionnel> userProfileBlocFonctionnels) {
        this.setUserProfileBlocFonctionnels(userProfileBlocFonctionnels);
        return this;
    }

    public BlocFonctionnel addUserProfileBlocFonctionnels(UserProfileBlocFonctionnel userProfileBlocFonctionnel) {
        this.userProfileBlocFonctionnels.add(userProfileBlocFonctionnel);
        userProfileBlocFonctionnel.setBlocFonctionnel(this);
        return this;
    }

    public BlocFonctionnel removeUserProfileBlocFonctionnels(UserProfileBlocFonctionnel userProfileBlocFonctionnel) {
        this.userProfileBlocFonctionnels.remove(userProfileBlocFonctionnel);
        userProfileBlocFonctionnel.setBlocFonctionnel(null);
        return this;
    }

    public Long getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(Long serviceUser) {
        this.serviceId = serviceUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlocFonctionnel)) {
            return false;
        }
        return getId() != null && getId().equals(((BlocFonctionnel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlocFonctionnel{" +
            "id=" + getId() +
            ", libelleBloc='" + getLibelleBloc() + "'" +
            ", dateAjoutBloc='" + getDateAjoutBloc() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
