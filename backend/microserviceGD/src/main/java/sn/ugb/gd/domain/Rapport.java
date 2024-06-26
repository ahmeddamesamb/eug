package sn.ugb.gd.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rapport.
 */
@Entity
@Table(name = "rapport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rapport")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rapport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_rapport")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelleRapport;

    @Column(name = "description_rapport")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String descriptionRapport;

    @Column(name = "contenu_rapport")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contenuRapport;

    @Column(name = "date_redaction")
    private Instant dateRedaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rapport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleRapport() {
        return this.libelleRapport;
    }

    public Rapport libelleRapport(String libelleRapport) {
        this.setLibelleRapport(libelleRapport);
        return this;
    }

    public void setLibelleRapport(String libelleRapport) {
        this.libelleRapport = libelleRapport;
    }

    public String getDescriptionRapport() {
        return this.descriptionRapport;
    }

    public Rapport descriptionRapport(String descriptionRapport) {
        this.setDescriptionRapport(descriptionRapport);
        return this;
    }

    public void setDescriptionRapport(String descriptionRapport) {
        this.descriptionRapport = descriptionRapport;
    }

    public String getContenuRapport() {
        return this.contenuRapport;
    }

    public Rapport contenuRapport(String contenuRapport) {
        this.setContenuRapport(contenuRapport);
        return this;
    }

    public void setContenuRapport(String contenuRapport) {
        this.contenuRapport = contenuRapport;
    }

    public Instant getDateRedaction() {
        return this.dateRedaction;
    }

    public Rapport dateRedaction(Instant dateRedaction) {
        this.setDateRedaction(dateRedaction);
        return this;
    }

    public void setDateRedaction(Instant dateRedaction) {
        this.dateRedaction = dateRedaction;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rapport)) {
            return false;
        }
        return getId() != null && getId().equals(((Rapport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rapport{" +
            "id=" + getId() +
            ", libelleRapport='" + getLibelleRapport() + "'" +
            ", descriptionRapport='" + getDescriptionRapport() + "'" +
            ", contenuRapport='" + getContenuRapport() + "'" +
            ", dateRedaction='" + getDateRedaction() + "'" +
            "}";
    }
}
