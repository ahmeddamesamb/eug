package sn.ugb.gir.service.dto;

import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.InscriptionAdministrative;

public class InformationsIADTO {

    private InscriptionAdministrative inscriptionAdministrative;

    private InformationPersonnelle informationPersonnelle;

    public InformationsIADTO() {
    }

    public InformationsIADTO(InscriptionAdministrative inscriptionAdministrative, InformationPersonnelle informationPersonnelle) {
        this.inscriptionAdministrative = inscriptionAdministrative;
        this.informationPersonnelle = informationPersonnelle;
    }

    public InscriptionAdministrative getInscriptionAdministrative() {
        return inscriptionAdministrative;
    }

    public void setInscriptionAdministrative(InscriptionAdministrative inscriptionAdministrative) {
        this.inscriptionAdministrative = inscriptionAdministrative;
    }

    public InformationPersonnelle getInformationPersonnelle() {
        return informationPersonnelle;
    }

    public void setInformationPersonnelle(InformationPersonnelle informationPersonnelle) {
        this.informationPersonnelle = informationPersonnelle;
    }

    @Override
    public String toString() {
        return "InformationsIADTO{" +
            "inscriptionAdministrative=" + inscriptionAdministrative +
            ", informationPersonnelle=" + informationPersonnelle +
            '}';
    }


}
