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
 * A HistoriqueConnexion.
 */
@Table("historique_connexion")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "historiqueconnexion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueConnexion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("date_debut_connexion")
    private LocalDate dateDebutConnexion;

    @NotNull(message = "must not be null")
    @Column("date_fin_connexion")
    private LocalDate dateFinConnexion;

    @Column("adresse_ip")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String adresseIp;

    @NotNull(message = "must not be null")
    @Column("actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @Transient
    @JsonIgnoreProperties(value = { "user", "historiqueConnexions", "userProfiles", "infoUserRessources" }, allowSetters = true)
    private InfosUser infoUser;

    @Column("info_user_id")
    private Long infoUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriqueConnexion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebutConnexion() {
        return this.dateDebutConnexion;
    }

    public HistoriqueConnexion dateDebutConnexion(LocalDate dateDebutConnexion) {
        this.setDateDebutConnexion(dateDebutConnexion);
        return this;
    }

    public void setDateDebutConnexion(LocalDate dateDebutConnexion) {
        this.dateDebutConnexion = dateDebutConnexion;
    }

    public LocalDate getDateFinConnexion() {
        return this.dateFinConnexion;
    }

    public HistoriqueConnexion dateFinConnexion(LocalDate dateFinConnexion) {
        this.setDateFinConnexion(dateFinConnexion);
        return this;
    }

    public void setDateFinConnexion(LocalDate dateFinConnexion) {
        this.dateFinConnexion = dateFinConnexion;
    }

    public String getAdresseIp() {
        return this.adresseIp;
    }

    public HistoriqueConnexion adresseIp(String adresseIp) {
        this.setAdresseIp(adresseIp);
        return this;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public HistoriqueConnexion actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public InfosUser getInfoUser() {
        return this.infoUser;
    }

    public void setInfoUser(InfosUser infosUser) {
        this.infoUser = infosUser;
        this.infoUserId = infosUser != null ? infosUser.getId() : null;
    }

    public HistoriqueConnexion infoUser(InfosUser infosUser) {
        this.setInfoUser(infosUser);
        return this;
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
        if (!(o instanceof HistoriqueConnexion)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriqueConnexion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueConnexion{" +
            "id=" + getId() +
            ", dateDebutConnexion='" + getDateDebutConnexion() + "'" +
            ", dateFinConnexion='" + getDateFinConnexion() + "'" +
            ", adresseIp='" + getAdresseIp() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
