package sn.ugb.gir.service.dto;

import java.io.Serializable;
import java.util.List;

public class DossierEtudiantDTO implements Serializable {

    private InformationPersonnelleDTO informationPersonnelle;

    private List<PaiementFraisDTO> paiementFrais;

    private FormationPriveeDTO formationPrivee;

    private List<ProcessusInscriptionAdministrativeDTO> processusInscriptionAdministratives;

    public InformationPersonnelleDTO getInformationPersonnelle() {
        return informationPersonnelle;
    }

    public void setInformationPersonnelle(InformationPersonnelleDTO informationPersonnelle) {
        this.informationPersonnelle = informationPersonnelle;
    }

    public List<PaiementFraisDTO> getPaiementFrais() {
        return paiementFrais;
    }

    public void setPaiementFrais(List<PaiementFraisDTO> paiementFrais) {
        this.paiementFrais = paiementFrais;
    }

    public FormationPriveeDTO getFormationPrivee() {
        return formationPrivee;
    }

    public void setFormationPrivee(FormationPriveeDTO formationPrivee) {
        this.formationPrivee = formationPrivee;
    }

    public List<ProcessusInscriptionAdministrativeDTO> getProcessusInscriptionAdministratives() {
        return processusInscriptionAdministratives;
    }

    public void setProcessusInscriptionAdministratives(List<ProcessusInscriptionAdministrativeDTO> processusInscriptionAdministratives) {
        this.processusInscriptionAdministratives = processusInscriptionAdministratives;
    }
}
