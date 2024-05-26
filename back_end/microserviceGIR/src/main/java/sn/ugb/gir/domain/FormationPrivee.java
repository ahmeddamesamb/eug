package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FormationPrivee.
 */
@Entity
@Table(name = "formation_privee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormationPrivee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre_mensualites", nullable = false)
    private Integer nombreMensualites;

    @Column(name = "paiement_premier_mois_yn")
    private Integer paiementPremierMoisYN;

    @Column(name = "paiement_dernier_mois_yn")
    private Integer paiementDernierMoisYN;

    @Column(name = "frais_dossier_yn")
    private Integer fraisDossierYN;

    @NotNull
    @Column(name = "cout_total", nullable = false)
    private Float coutTotal;

    @NotNull
    @Column(name = "mensualite", nullable = false)
    private Float mensualite;

    @JsonIgnoreProperties(value = { "niveau", "specialite", "formationAutorisees", "formationPrivee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Formation formation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormationPrivee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNombreMensualites() {
        return this.nombreMensualites;
    }

    public FormationPrivee nombreMensualites(Integer nombreMensualites) {
        this.setNombreMensualites(nombreMensualites);
        return this;
    }

    public void setNombreMensualites(Integer nombreMensualites) {
        this.nombreMensualites = nombreMensualites;
    }

    public Integer getPaiementPremierMoisYN() {
        return this.paiementPremierMoisYN;
    }

    public FormationPrivee paiementPremierMoisYN(Integer paiementPremierMoisYN) {
        this.setPaiementPremierMoisYN(paiementPremierMoisYN);
        return this;
    }

    public void setPaiementPremierMoisYN(Integer paiementPremierMoisYN) {
        this.paiementPremierMoisYN = paiementPremierMoisYN;
    }

    public Integer getPaiementDernierMoisYN() {
        return this.paiementDernierMoisYN;
    }

    public FormationPrivee paiementDernierMoisYN(Integer paiementDernierMoisYN) {
        this.setPaiementDernierMoisYN(paiementDernierMoisYN);
        return this;
    }

    public void setPaiementDernierMoisYN(Integer paiementDernierMoisYN) {
        this.paiementDernierMoisYN = paiementDernierMoisYN;
    }

    public Integer getFraisDossierYN() {
        return this.fraisDossierYN;
    }

    public FormationPrivee fraisDossierYN(Integer fraisDossierYN) {
        this.setFraisDossierYN(fraisDossierYN);
        return this;
    }

    public void setFraisDossierYN(Integer fraisDossierYN) {
        this.fraisDossierYN = fraisDossierYN;
    }

    public Float getCoutTotal() {
        return this.coutTotal;
    }

    public FormationPrivee coutTotal(Float coutTotal) {
        this.setCoutTotal(coutTotal);
        return this;
    }

    public void setCoutTotal(Float coutTotal) {
        this.coutTotal = coutTotal;
    }

    public Float getMensualite() {
        return this.mensualite;
    }

    public FormationPrivee mensualite(Float mensualite) {
        this.setMensualite(mensualite);
        return this;
    }

    public void setMensualite(Float mensualite) {
        this.mensualite = mensualite;
    }

    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public FormationPrivee formation(Formation formation) {
        this.setFormation(formation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormationPrivee)) {
            return false;
        }
        return getId() != null && getId().equals(((FormationPrivee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationPrivee{" +
            "id=" + getId() +
            ", nombreMensualites=" + getNombreMensualites() +
            ", paiementPremierMoisYN=" + getPaiementPremierMoisYN() +
            ", paiementDernierMoisYN=" + getPaiementDernierMoisYN() +
            ", fraisDossierYN=" + getFraisDossierYN() +
            ", coutTotal=" + getCoutTotal() +
            ", mensualite=" + getMensualite() +
            "}";
    }
}
