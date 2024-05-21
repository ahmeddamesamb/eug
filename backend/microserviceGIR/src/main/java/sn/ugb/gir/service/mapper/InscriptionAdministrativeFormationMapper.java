package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;

/**
 * Mapper for the entity {@link InscriptionAdministrativeFormation} and its DTO {@link InscriptionAdministrativeFormationDTO}.
 */
@Mapper(componentModel = "spring")
public interface InscriptionAdministrativeFormationMapper
    extends EntityMapper<InscriptionAdministrativeFormationDTO, InscriptionAdministrativeFormation> {
    @Mapping(target = "inscriptionAdministrative", source = "inscriptionAdministrative", qualifiedByName = "inscriptionAdministrativeId")
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationId")
    InscriptionAdministrativeFormationDTO toDto(InscriptionAdministrativeFormation s);

    @Named("inscriptionAdministrativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InscriptionAdministrativeDTO toDtoInscriptionAdministrativeId(InscriptionAdministrative inscriptionAdministrative);

    @Named("formationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormationDTO toDtoFormationId(Formation formation);
}
