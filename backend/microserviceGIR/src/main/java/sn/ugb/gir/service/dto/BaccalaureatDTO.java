package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Baccalaureat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BaccalaureatDTO implements Serializable {

    private Long id;

    private String origineScolaire;

    private LocalDate anneeBac;

    private Integer numeroTable;

    private String natureBac;

    private String mentionBac;

    private Float moyenneSelectionBac;

    private Float moyenneBac;

    private Boolean actifYN;

    private EtudiantDTO etudiant;

    private SerieDTO serie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigineScolaire() {
        return origineScolaire;
    }

    public void setOrigineScolaire(String origineScolaire) {
        this.origineScolaire = origineScolaire;
    }

    public LocalDate getAnneeBac() {
        return anneeBac;
    }

    public void setAnneeBac(LocalDate anneeBac) {
        this.anneeBac = anneeBac;
    }

    public Integer getNumeroTable() {
        return numeroTable;
    }

    public void setNumeroTable(Integer numeroTable) {
        this.numeroTable = numeroTable;
    }

    public String getNatureBac() {
        return natureBac;
    }

    public void setNatureBac(String natureBac) {
        this.natureBac = natureBac;
    }

    public String getMentionBac() {
        return mentionBac;
    }

    public void setMentionBac(String mentionBac) {
        this.mentionBac = mentionBac;
    }

    public Float getMoyenneSelectionBac() {
        return moyenneSelectionBac;
    }

    public void setMoyenneSelectionBac(Float moyenneSelectionBac) {
        this.moyenneSelectionBac = moyenneSelectionBac;
    }

    public Float getMoyenneBac() {
        return moyenneBac;
    }

    public void setMoyenneBac(Float moyenneBac) {
        this.moyenneBac = moyenneBac;
    }

    public Boolean getActifYN() {
        return actifYN;
    }

    public void setActifYN(Boolean actifYN) {
        this.actifYN = actifYN;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    public SerieDTO getSerie() {
        return serie;
    }

    public void setSerie(SerieDTO serie) {
        this.serie = serie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaccalaureatDTO)) {
            return false;
        }

        BaccalaureatDTO baccalaureatDTO = (BaccalaureatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, baccalaureatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaccalaureatDTO{" +
            "id=" + getId() +
            ", origineScolaire='" + getOrigineScolaire() + "'" +
            ", anneeBac='" + getAnneeBac() + "'" +
            ", numeroTable=" + getNumeroTable() +
            ", natureBac='" + getNatureBac() + "'" +
            ", mentionBac='" + getMentionBac() + "'" +
            ", moyenneSelectionBac=" + getMoyenneSelectionBac() +
            ", moyenneBac=" + getMoyenneBac() +
            ", actifYN='" + getActifYN() + "'" +
            ", etudiant=" + getEtudiant() +
            ", serie=" + getSerie() +
            "}";
    }
}
