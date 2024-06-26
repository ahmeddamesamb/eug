package sn.ugb.gir.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Baccalaureat.
 */
@Entity
@Table(name = "baccalaureat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "baccalaureat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Baccalaureat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "origine_scolaire")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String origineScolaire;

    @Column(name = "annee_bac")
    private LocalDate anneeBac;

    @Column(name = "numero_table")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numeroTable;

    @Column(name = "nature_bac")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String natureBac;

    @Column(name = "mention_bac")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mentionBac;

    @Column(name = "moyenne_selection_bac")
    private Float moyenneSelectionBac;

    @Column(name = "moyenne_bac")
    private Float moyenneBac;

    @Column(name = "actif_yn")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean actifYN;

    @JsonIgnoreProperties(
        value = {
            "region",
            "typeSelection",
            "lycee",
            "informationPersonnelle",
            "baccalaureat",
            "disciplineSportiveEtudiants",
            "inscriptionAdministratives",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "baccalaureats" }, allowSetters = true)
    private Serie serie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Baccalaureat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigineScolaire() {
        return this.origineScolaire;
    }

    public Baccalaureat origineScolaire(String origineScolaire) {
        this.setOrigineScolaire(origineScolaire);
        return this;
    }

    public void setOrigineScolaire(String origineScolaire) {
        this.origineScolaire = origineScolaire;
    }

    public LocalDate getAnneeBac() {
        return this.anneeBac;
    }

    public Baccalaureat anneeBac(LocalDate anneeBac) {
        this.setAnneeBac(anneeBac);
        return this;
    }

    public void setAnneeBac(LocalDate anneeBac) {
        this.anneeBac = anneeBac;
    }

    public Integer getNumeroTable() {
        return this.numeroTable;
    }

    public Baccalaureat numeroTable(Integer numeroTable) {
        this.setNumeroTable(numeroTable);
        return this;
    }

    public void setNumeroTable(Integer numeroTable) {
        this.numeroTable = numeroTable;
    }

    public String getNatureBac() {
        return this.natureBac;
    }

    public Baccalaureat natureBac(String natureBac) {
        this.setNatureBac(natureBac);
        return this;
    }

    public void setNatureBac(String natureBac) {
        this.natureBac = natureBac;
    }

    public String getMentionBac() {
        return this.mentionBac;
    }

    public Baccalaureat mentionBac(String mentionBac) {
        this.setMentionBac(mentionBac);
        return this;
    }

    public void setMentionBac(String mentionBac) {
        this.mentionBac = mentionBac;
    }

    public Float getMoyenneSelectionBac() {
        return this.moyenneSelectionBac;
    }

    public Baccalaureat moyenneSelectionBac(Float moyenneSelectionBac) {
        this.setMoyenneSelectionBac(moyenneSelectionBac);
        return this;
    }

    public void setMoyenneSelectionBac(Float moyenneSelectionBac) {
        this.moyenneSelectionBac = moyenneSelectionBac;
    }

    public Float getMoyenneBac() {
        return this.moyenneBac;
    }

    public Baccalaureat moyenneBac(Float moyenneBac) {
        this.setMoyenneBac(moyenneBac);
        return this;
    }

    public void setMoyenneBac(Float moyenneBac) {
        this.moyenneBac = moyenneBac;
    }

    public Boolean getActifYN() {
        return this.actifYN;
    }

    public Baccalaureat actifYN(Boolean actifYN) {
        this.setActifYN(actifYN);
        return this;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Baccalaureat etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public Serie getSerie() {
        return this.serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Baccalaureat serie(Serie serie) {
        this.setSerie(serie);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Baccalaureat)) {
            return false;
        }
        return getId() != null && getId().equals(((Baccalaureat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Baccalaureat{" +
            "id=" + getId() +
            ", origineScolaire='" + getOrigineScolaire() + "'" +
            ", anneeBac='" + getAnneeBac() + "'" +
            ", numeroTable=" + getNumeroTable() +
            ", natureBac='" + getNatureBac() + "'" +
            ", mentionBac='" + getMentionBac() + "'" +
            ", moyenneSelectionBac=" + getMoyenneSelectionBac() +
            ", moyenneBac=" + getMoyenneBac() +
            ", actifYN='" + getActifYN() + "'" +
            "}";
    }
}
