package sn.ugb.gir.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.ugb.gir.domain.Etudiant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtudiantDTO implements Serializable {

    private Long id;

    @NotNull
    private String codeEtu;

    private String ine;

    private Integer codeBU;

    private String emailUGB;

    @NotNull
    private LocalDate dateNaissEtu;

    @NotNull
    private String lieuNaissEtu;

    @NotNull
    private String sexe;

    private String numDocIdentite;

    @NotNull
    private Integer assimileYN;

    @NotNull
    private Integer exonereYN;

    private RegionDTO region;

    private TypeSelectionDTO typeSelection;

    private LyceeDTO lycee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeEtu() {
        return codeEtu;
    }

    public void setCodeEtu(String codeEtu) {
        this.codeEtu = codeEtu;
    }

    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public Integer getCodeBU() {
        return codeBU;
    }

    public void setCodeBU(Integer codeBU) {
        this.codeBU = codeBU;
    }

    public String getEmailUGB() {
        return emailUGB;
    }

    public void setEmailUGB(String emailUGB) {
        this.emailUGB = emailUGB;
    }

    public LocalDate getDateNaissEtu() {
        return dateNaissEtu;
    }

    public void setDateNaissEtu(LocalDate dateNaissEtu) {
        this.dateNaissEtu = dateNaissEtu;
    }

    public String getLieuNaissEtu() {
        return lieuNaissEtu;
    }

    public void setLieuNaissEtu(String lieuNaissEtu) {
        this.lieuNaissEtu = lieuNaissEtu;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNumDocIdentite() {
        return numDocIdentite;
    }

    public void setNumDocIdentite(String numDocIdentite) {
        this.numDocIdentite = numDocIdentite;
    }

    public Integer getAssimileYN() {
        return assimileYN;
    }

    public void setAssimileYN(Integer assimileYN) {
        this.assimileYN = assimileYN;
    }

    public Integer getExonereYN() {
        return exonereYN;
    }

    public void setExonereYN(Integer exonereYN) {
        this.exonereYN = exonereYN;
    }

    public RegionDTO getRegion() {
        return region;
    }

    public void setRegion(RegionDTO region) {
        this.region = region;
    }

    public TypeSelectionDTO getTypeSelection() {
        return typeSelection;
    }

    public void setTypeSelection(TypeSelectionDTO typeSelection) {
        this.typeSelection = typeSelection;
    }

    public LyceeDTO getLycee() {
        return lycee;
    }

    public void setLycee(LyceeDTO lycee) {
        this.lycee = lycee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtudiantDTO)) {
            return false;
        }

        EtudiantDTO etudiantDTO = (EtudiantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, etudiantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtudiantDTO{" +
            "id=" + getId() +
            ", codeEtu='" + getCodeEtu() + "'" +
            ", ine='" + getIne() + "'" +
            ", codeBU=" + getCodeBU() +
            ", emailUGB='" + getEmailUGB() + "'" +
            ", dateNaissEtu='" + getDateNaissEtu() + "'" +
            ", lieuNaissEtu='" + getLieuNaissEtu() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", numDocIdentite='" + getNumDocIdentite() + "'" +
            ", assimileYN=" + getAssimileYN() +
            ", exonereYN=" + getExonereYN() +
            ", region=" + getRegion() +
            ", typeSelection=" + getTypeSelection() +
            ", lycee=" + getLycee() +
            "}";
    }
}
