package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Doctorat;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.InscriptionDoctorat;
import sn.ugb.gir.service.dto.DoctoratDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;
import sn.ugb.gir.service.dto.InscriptionDoctoratDTO;

/**
 * Mapper for the entity {@link InscriptionDoctorat} and its DTO {@link InscriptionDoctoratDTO}.
 */
@Mapper(componentModel = "spring")
public interface InscriptionDoctoratMapper extends EntityMapper<InscriptionDoctoratDTO, InscriptionDoctorat> {
    @Mapping(target = "doctorat", source = "doctorat", qualifiedByName = "doctoratId")
    @Mapping(
        target = "inscriptionAdministrativeFormation",
        source = "inscriptionAdministrativeFormation",
        qualifiedByName = "inscriptionAdministrativeFormationId"
    )
    InscriptionDoctoratDTO toDto(InscriptionDoctorat s);

    @Named("doctoratId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctoratDTO toDtoDoctoratId(Doctorat doctorat);

    @Named("inscriptionAdministrativeFormationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InscriptionAdministrativeFormationDTO toDtoInscriptionAdministrativeFormationId(
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation
    );
}
