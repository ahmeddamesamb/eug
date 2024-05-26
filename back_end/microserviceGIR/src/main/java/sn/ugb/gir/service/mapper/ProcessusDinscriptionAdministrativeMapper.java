package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.domain.ProcessusDinscriptionAdministrative;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;
import sn.ugb.gir.service.dto.ProcessusDinscriptionAdministrativeDTO;

/**
 * Mapper for the entity {@link ProcessusDinscriptionAdministrative} and its DTO {@link ProcessusDinscriptionAdministrativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessusDinscriptionAdministrativeMapper
    extends EntityMapper<ProcessusDinscriptionAdministrativeDTO, ProcessusDinscriptionAdministrative> {
    @Mapping(target = "inscriptionAdministrative", source = "inscriptionAdministrative", qualifiedByName = "inscriptionAdministrativeId")
    ProcessusDinscriptionAdministrativeDTO toDto(ProcessusDinscriptionAdministrative s);

    @Named("inscriptionAdministrativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InscriptionAdministrativeDTO toDtoInscriptionAdministrativeId(InscriptionAdministrative inscriptionAdministrative);
}
