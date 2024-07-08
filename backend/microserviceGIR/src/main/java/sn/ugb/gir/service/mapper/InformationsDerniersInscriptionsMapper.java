package sn.ugb.gir.service.mapper;


import org.mapstruct.*;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.InformationsDerniersInscriptions;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.service.dto.InformationsDerniersInscriptionsDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;

/**
 * Mapper for the entity {@link InformationsDerniersInscriptions} and its DTO {@link InformationsDerniersInscriptionsDTO}.
 */
@Mapper(componentModel = "spring" , uses = { InformationPersonnelleMapper.class, InscriptionAdministrativeFormationMapper.class })
public interface InformationsDerniersInscriptionsMapper
    extends EntityMapper<InformationsDerniersInscriptionsDTO, InformationsDerniersInscriptions> {
    @Mapping(target = "inscriptionAdministrativeFormation", source = "inscriptionAdministrativeFormation")
    @Mapping(target = "informationPersonnelle", source = "informationPersonnelle")
    InformationsDerniersInscriptionsDTO toDto(InformationsDerniersInscriptions s);

    @Named("inscriptionAdministrativeFormationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "inscriptionPrincipaleYN", source = "inscriptionPrincipaleYN")
    @Mapping(target = "inscriptionAnnuleeYN", source = "inscriptionAnnuleeYN")
    @Mapping(target = "exonereYN", source = "exonereYN")
    @Mapping(target = "paiementFraisOblYN", source = "paiementFraisOblYN")
    @Mapping(target = "paiementFraisIntegergYN", source = "paiementFraisIntegergYN")
    @Mapping(target = "certificatDelivreYN", source = "certificatDelivreYN")
    @Mapping(target = "dateChoixFormation", source = "dateChoixFormation")
    @Mapping(target = "dateValidationInscription", source = "dateValidationInscription")
    @Mapping(target = "inscriptionAdministrative", source = "inscriptionAdministrative")
    @Mapping(target = "formation", source = "formation")
    InscriptionAdministrativeFormationDTO toDtoInscriptionAdministrativeFormationId(InscriptionAdministrativeFormation inscriptionAdministrativeFormation);


    @Named("informationPersonnelleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomEtu", source = "nomEtu")
    @Mapping(target = "nomJeuneFilleEtu", source = "nomJeuneFilleEtu")
    @Mapping(target = "prenomEtu", source = "prenomEtu")
    @Mapping(target = "statutMarital", source = "statutMarital")
    @Mapping(target = "regime", source = "regime")
    @Mapping(target = "profession", source = "profession")
    @Mapping(target = "adresseEtu", source = "adresseEtu")
    @Mapping(target = "telEtu", source = "telEtu")
    @Mapping(target = "emailEtu", source = "emailEtu")
    @Mapping(target = "adresseParent", source = "adresseParent")
    @Mapping(target = "telParent", source = "telParent")
    @Mapping(target = "emailParent", source = "emailParent")
    @Mapping(target = "nomParent", source = "nomParent")
    @Mapping(target = "prenomParent", source = "prenomParent")
    @Mapping(target = "handicapYN", source = "handicapYN")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "ordiPersoYN", source = "ordiPersoYN")
    @Mapping(target = "derniereModification", source = "derniereModification")
    @Mapping(target = "emailUser", source = "emailUser")
    @Mapping(target = "typeHandicap", source = "typeHandicap")
    @Mapping(target = "typeBourse", source = "typeBourse")
    @Mapping(target = "etudiant", source = "etudiant")
    InformationPersonnelleDTO toDtoInformationPersonnelleId(InformationPersonnelle informationPersonnelle);


}
