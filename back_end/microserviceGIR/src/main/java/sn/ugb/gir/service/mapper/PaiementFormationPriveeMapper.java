package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.Operateur;
import sn.ugb.gir.domain.PaiementFormationPrivee;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;
import sn.ugb.gir.service.dto.OperateurDTO;
import sn.ugb.gir.service.dto.PaiementFormationPriveeDTO;

/**
 * Mapper for the entity {@link PaiementFormationPrivee} and its DTO {@link PaiementFormationPriveeDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementFormationPriveeMapper extends EntityMapper<PaiementFormationPriveeDTO, PaiementFormationPrivee> {
    @Mapping(
        target = "inscriptionAdministrativeFormation",
        source = "inscriptionAdministrativeFormation",
        qualifiedByName = "inscriptionAdministrativeFormationId"
    )
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "operateurId")
    PaiementFormationPriveeDTO toDto(PaiementFormationPrivee s);

    @Named("inscriptionAdministrativeFormationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InscriptionAdministrativeFormationDTO toDtoInscriptionAdministrativeFormationId(
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation
    );

    @Named("operateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OperateurDTO toDtoOperateurId(Operateur operateur);
}
