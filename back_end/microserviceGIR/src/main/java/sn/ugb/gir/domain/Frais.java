package sn.ugb.gir.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.ugb.gir.domain.enumeration.Cycle;

/**
 * A Frais.
 */
@Entity
@Table(name = "frais")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Frais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(5000) //Ajouter
    @Column(name = "valeur_frais", nullable = false)
    private Double valeurFrais;

    @NotNull //Ajouter
    @Column(name = "description_frais", nullable = false)
    private String descriptionFrais;

    @Column(name = "frais_pour_assimile_yn")
    private Integer fraisPourAssimileYN;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cycle", nullable = false)
    private Cycle cycle;

    @Column(name = "dia")
    private Double dia;

    @Column(name = "dip")
    private Double dip;

    @Column(name = "dip_privee")
    private Float dipPrivee;

    @NotNull
    @Column(name = "date_application", nullable = false)
    private LocalDate dateApplication;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull
    @Column(name = "est_en_application_yn", nullable = false)
    private Integer estEnApplicationYN;

    @NotNull //Ajouter
    @ManyToOne(fetch = FetchType.LAZY)
    private TypeFrais typeFrais;

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

    public Integer getFraisPourAssimileYN() {
        return this.fraisPourAssimileYN;
    }

    public Frais fraisPourAssimileYN(Integer fraisPourAssimileYN) {
        this.setFraisPourAssimileYN(fraisPourAssimileYN);
        return this;
    }

    public void setFraisPourAssimileYN(Integer fraisPourAssimileYN) {
        this.fraisPourAssimileYN = fraisPourAssimileYN;
    }

    public Cycle getCycle() {
        return this.cycle;
    }

    public Frais cycle(Cycle cycle) {
        this.setCycle(cycle);
        return this;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
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

    public Float getDipPrivee() {
        return this.dipPrivee;
    }

    public Frais dipPrivee(Float dipPrivee) {
        this.setDipPrivee(dipPrivee);
        return this;
    }

    public void setDipPrivee(Float dipPrivee) {
        this.dipPrivee = dipPrivee;
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

    public Integer getEstEnApplicationYN() {
        return this.estEnApplicationYN;
    }

    public Frais estEnApplicationYN(Integer estEnApplicationYN) {
        this.setEstEnApplicationYN(estEnApplicationYN);
        return this;
    }

    public void setEstEnApplicationYN(Integer estEnApplicationYN) {
        this.estEnApplicationYN = estEnApplicationYN;
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
            ", fraisPourAssimileYN=" + getFraisPourAssimileYN() +
            ", cycle='" + getCycle() + "'" +
            ", dia=" + getDia() +
            ", dip=" + getDip() +
            ", dipPrivee=" + getDipPrivee() +
            ", dateApplication='" + getDateApplication() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", estEnApplicationYN=" + getEstEnApplicationYN() +
            "}";
    }
}
