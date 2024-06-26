package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Frais.
 */
@Entity
@Table(name = "frais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "frais")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Frais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "valeur_frais", nullable = false)
    private Double valeurFrais;

    @Column(name = "description_frais")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String descriptionFrais;

    @Column(name = "frais_pour_assimile_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean fraisPourAssimileYN;

    @Column(name = "frais_pour_exonerer_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean fraisPourExonererYN;

    @Column(name = "dia")
    private Double dia;

    @Column(name = "dip")
    private Double dip;

    @Column(name = "frais_privee")
    private Double fraisPrivee;

    @NotNull
    @Column(name = "date_application", nullable = false)
    private LocalDate dateApplication;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull
    @Column(name = "est_en_application_yn", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean estEnApplicationYN;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "frais" }, allowSetters = true)
    private TypeFrais typeFrais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "niveaux", "frais" }, allowSetters = true)
    private Cycle typeCycle;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_frais__universite",
        joinColumns = @JoinColumn(name = "frais_id"),
        inverseJoinColumns = @JoinColumn(name = "universite_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ministere", "ufrs", "frais" }, allowSetters = true)
    private Set<Universite> universites = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "frais")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "frais", "operateur", "inscriptionAdministrativeFormation" }, allowSetters = true)
    private Set<PaiementFrais> paiementFrais = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Frais id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValeurFrais() {
        return this.valeurFrais;
    }

    public Frais valeurFrais(Double valeurFrais) {
        this.setValeurFrais(valeurFrais);
        return this;
    }

    public void setValeurFrais(Double valeurFrais) {
        this.valeurFrais = valeurFrais;
    }

    public String getDescriptionFrais() {
        return this.descriptionFrais;
    }

    public Frais descriptionFrais(String descriptionFrais) {
        this.setDescriptionFrais(descriptionFrais);
        return this;
    }

    public void setDescriptionFrais(String descriptionFrais) {
        this.descriptionFrais = descriptionFrais;
    }

    public Boolean getFraisPourAssimileYN() {
        return this.fraisPourAssimileYN;
    }

    public Frais fraisPourAssimileYN(Boolean fraisPourAssimileYN) {
        this.setFraisPourAssimileYN(fraisPourAssimileYN);
        return this;
    }

    public void setFraisPourAssimileYN(Boolean fraisPourAssimileYN) {
        this.fraisPourAssimileYN = fraisPourAssimileYN;
    }

    public Boolean getFraisPourExonererYN() {
        return this.fraisPourExonererYN;
    }

    public Frais fraisPourExonererYN(Boolean fraisPourExonererYN) {
        this.setFraisPourExonererYN(fraisPourExonererYN);
        return this;
    }

    public void setFraisPourExonererYN(Boolean fraisPourExonererYN) {
        this.fraisPourExonererYN = fraisPourExonererYN;
    }

    public Double getDia() {
        return this.dia;
    }

    public Frais dia(Double dia) {
        this.setDia(dia);
        return this;
    }

    public void setDia(Double dia) {
        this.dia = dia;
    }

    public Double getDip() {
        return this.dip;
    }

    public Frais dip(Double dip) {
        this.setDip(dip);
        return this;
    }

    public void setDip(Double dip) {
        this.dip = dip;
    }

    public Double getFraisPrivee() {
        return this.fraisPrivee;
    }

    public Frais fraisPrivee(Double fraisPrivee) {
        this.setFraisPrivee(fraisPrivee);
        return this;
    }

    public void setFraisPrivee(Double fraisPrivee) {
        this.fraisPrivee = fraisPrivee;
    }

    public LocalDate getDateApplication() {
        return this.dateApplication;
    }

    public Frais dateApplication(LocalDate dateApplication) {
        this.setDateApplication(dateApplication);
        return this;
    }

    public void setDateApplication(LocalDate dateApplication) {
        this.dateApplication = dateApplication;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Frais dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getEstEnApplicationYN() {
        return this.estEnApplicationYN;
    }

    public Frais estEnApplicationYN(Boolean estEnApplicationYN) {
        this.setEstEnApplicationYN(estEnApplicationYN);
        return this;
    }

    public void setEstEnApplicationYN(Boolean estEnApplicationYN) {
        this.estEnApplicationYN = estEnApplicationYN;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Frais actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public TypeFrais getTypeFrais() {
        return this.typeFrais;
    }

    public void setTypeFrais(TypeFrais typeFrais) {
        this.typeFrais = typeFrais;
    }

    public Frais typeFrais(TypeFrais typeFrais) {
        this.setTypeFrais(typeFrais);
        return this;
    }

    public Cycle getTypeCycle() {
        return this.typeCycle;
    }

    public void setTypeCycle(Cycle cycle) {
        this.typeCycle = cycle;
    }

    public Frais typeCycle(Cycle cycle) {
        this.setTypeCycle(cycle);
        return this;
    }

    public Set<Universite> getUniversites() {
        return this.universites;
    }

    public void setUniversites(Set<Universite> universites) {
        this.universites = universites;
    }

    public Frais universites(Set<Universite> universites) {
        this.setUniversites(universites);
        return this;
    }

    public Frais addUniversite(Universite universite) {
        this.universites.add(universite);
        return this;
    }

    public Frais removeUniversite(Universite universite) {
        this.universites.remove(universite);
        return this;
    }

    public Set<PaiementFrais> getPaiementFrais() {
        return this.paiementFrais;
    }

    public void setPaiementFrais(Set<PaiementFrais> paiementFrais) {
        if (this.paiementFrais != null) {
            this.paiementFrais.forEach(i -> i.setFrais(null));
        }
        if (paiementFrais != null) {
            paiementFrais.forEach(i -> i.setFrais(this));
        }
        this.paiementFrais = paiementFrais;
    }

    public Frais paiementFrais(Set<PaiementFrais> paiementFrais) {
        this.setPaiementFrais(paiementFrais);
        return this;
    }

    public Frais addPaiementFrais(PaiementFrais paiementFrais) {
        this.paiementFrais.add(paiementFrais);
        paiementFrais.setFrais(this);
        return this;
    }

    public Frais removePaiementFrais(PaiementFrais paiementFrais) {
        this.paiementFrais.remove(paiementFrais);
        paiementFrais.setFrais(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Frais)) {
            return false;
        }
        return getId() != null && getId().equals(((Frais) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Frais{" +
            "id=" + getId() +
            ", valeurFrais=" + getValeurFrais() +
            ", descriptionFrais='" + getDescriptionFrais() + "'" +
            ", fraisPourAssimileYN='" + getFraisPourAssimileYN() + "'" +
            ", fraisPourExonererYN='" + getFraisPourExonererYN() + "'" +
            ", dia=" + getDia() +
            ", dip=" + getDip() +
            ", fraisPrivee=" + getFraisPrivee() +
            ", dateApplication='" + getDateApplication() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", estEnApplicationYN='" + getEstEnApplicationYN() + "'" +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
