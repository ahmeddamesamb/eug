package sn.ugb.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Ressource.
 */
@Table("ressource")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ressource")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ressource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("libelle")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelle;

    @NotNull(message = "must not be null")
    @Column("actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @Transient
    @JsonIgnoreProperties(value = { "infosUser", "ressource" }, allowSetters = true)
    private Set<InfoUserRessource> infoUserRessources = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ressource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Ressource libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Ressource actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Set<InfoUserRessource> getInfoUserRessources() {
        return this.infoUserRessources;
    }

    public void setInfoUserRessources(Set<InfoUserRessource> infoUserRessources) {
        if (this.infoUserRessources != null) {
            this.infoUserRessources.forEach(i -> i.setRessource(null));
        }
        if (infoUserRessources != null) {
            infoUserRessources.forEach(i -> i.setRessource(this));
        }
        this.infoUserRessources = infoUserRessources;
    }

    public Ressource infoUserRessources(Set<InfoUserRessource> infoUserRessources) {
        this.setInfoUserRessources(infoUserRessources);
        return this;
    }

    public Ressource addInfoUserRessources(InfoUserRessource infoUserRessource) {
        this.infoUserRessources.add(infoUserRessource);
        infoUserRessource.setRessource(this);
        return this;
    }

    public Ressource removeInfoUserRessources(InfoUserRessource infoUserRessource) {
        this.infoUserRessources.remove(infoUserRessource);
        infoUserRessource.setRessource(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ressource)) {
            return false;
        }
        return getId() != null && getId().equals(((Ressource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ressource{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
