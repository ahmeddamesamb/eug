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
 * A InfoUserRessource.
 */
@Table("info_user_ressource")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "infouserressource")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfoUserRessource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("date_ajout")
    private LocalDate dateAjout;

    @NotNull(message = "must not be null")
    @Column("en_cours_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean enCoursYN;

    @Transient
    @JsonIgnoreProperties(value = { "user", "historiqueConnexions", "userProfiles", "infoUserRessources" }, allowSetters = true)
    private InfosUser infosUser;

    @Transient
    @JsonIgnoreProperties(value = { "infoUserRessources" }, allowSetters = true)
    private Ressource ressource;

    @Column("infos_user_id")
    private Long infosUserId;

    @Column("ressource_id")
    private Long ressourceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfoUserRessource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAjout() {
        return this.dateAjout;
    }

    public InfoUserRessource dateAjout(LocalDate dateAjout) {
        this.setDateAjout(dateAjout);
        return this;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Boolean getEnCoursYN() {
        return this.enCoursYN;
    }

    public InfoUserRessource enCoursYN(Boolean enCoursYN) {
        this.setEnCoursYN(enCoursYN);
        return this;
    }

    public void setEnCoursYN(Boolean enCoursYN) {
        this.enCoursYN = enCoursYN;
    }

    public InfosUser getInfosUser() {
        return this.infosUser;
    }

    public void setInfosUser(InfosUser infosUser) {
        this.infosUser = infosUser;
        this.infosUserId = infosUser != null ? infosUser.getId() : null;
    }

    public InfoUserRessource infosUser(InfosUser infosUser) {
        this.setInfosUser(infosUser);
        return this;
    }

    public Ressource getRessource() {
        return this.ressource;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
        this.ressourceId = ressource != null ? ressource.getId() : null;
    }

    public InfoUserRessource ressource(Ressource ressource) {
        this.setRessource(ressource);
        return this;
    }

    public Long getInfosUserId() {
        return this.infosUserId;
    }

    public void setInfosUserId(Long infosUser) {
        this.infosUserId = infosUser;
    }

    public Long getRessourceId() {
        return this.ressourceId;
    }

    public void setRessourceId(Long ressource) {
        this.ressourceId = ressource;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoUserRessource)) {
            return false;
        }
        return getId() != null && getId().equals(((InfoUserRessource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoUserRessource{" +
            "id=" + getId() +
            ", dateAjout='" + getDateAjout() + "'" +
            ", enCoursYN='" + getEnCoursYN() + "'" +
            "}";
    }
}
