package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.service.dto.MinistereDTO;
import sn.ugb.gir.service.dto.UniversiteDTO;

/**
 * Mapper for the entity {@link Universite} and its DTO {@link UniversiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniversiteMapper extends EntityMapper<UniversiteDTO, Universite> {
    @Mapping(target = "ministere", source = "ministere", qualifiedByName = "ministereId")
    UniversiteDTO toDto(Universite s);

    @Named("ministereId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MinistereDTO toDtoMinistereId(Ministere ministere);
}
