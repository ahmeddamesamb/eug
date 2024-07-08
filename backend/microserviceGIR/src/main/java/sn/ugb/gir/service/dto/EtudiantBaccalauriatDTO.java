package sn.ugb.gir.service.dto;

public class EtudiantBaccalauriatDTO {

    private InformationPersonnelleDTO informationPersonnelle;
    private BaccalaureatDTO baccalaureatDTO;

    public BaccalaureatDTO getBaccalaureatDTO() {
        return baccalaureatDTO;
    }

    public void setBaccalaureatDTO(BaccalaureatDTO baccalaureatDTO) {
        this.baccalaureatDTO = baccalaureatDTO;
    }

    public InformationPersonnelleDTO getInformationPersonnelle() {
        return informationPersonnelle;
    }

    public void setInformationPersonnelle(InformationPersonnelleDTO informationPersonnelle) {
        this.informationPersonnelle = informationPersonnelle;
    }

    @Override
    public String toString() {
        return "EtudiantBaccalauriatDTO{" +
            "informationPersonnelle=" + informationPersonnelle +
            ", baccalaureatDTO=" + baccalaureatDTO +
            '}';
    }
}
