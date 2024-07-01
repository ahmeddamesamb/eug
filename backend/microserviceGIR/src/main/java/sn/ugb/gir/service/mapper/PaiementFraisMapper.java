package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.Operateur;
import sn.ugb.gir.domain.PaiementFrais;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;
import sn.ugb.gir.service.dto.OperateurDTO;
import sn.ugb.gir.service.dto.PaiementFraisDTO;

/**
 * Mapper for the entity {@link PaiementFrais} and its DTO {@link PaiementFraisDTO}.
 */
@Mapper(componentModel = "spring" , uses ={FraisMapper.class, OperateurMapper.class, InscriptionAdministrativeFormationMapper.class})
public interface PaiementFraisMapper extends EntityMapper<PaiementFraisDTO, PaiementFrais> {
    @Mapping(target = "frais", source = "frais")
    @Mapping(target = "operateur", source = "operateur")
    @Mapping(
        target = "inscriptionAdministrativeFormation",
        source = "inscriptionAdministrativeFormation"
    )
    PaiementFraisDTO toDto(PaiementFrais s);

    @Named("fraisId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FraisDTO toDtoFraisId(Frais frais);

    @Named("operateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OperateurDTO toDtoOperateurId(Operateur operateur);

    @Named("inscriptionAdministrativeFormationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InscriptionAdministrativeFormationDTO toDtoInscriptionAdministrativeFormationId(
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation
    );
}
