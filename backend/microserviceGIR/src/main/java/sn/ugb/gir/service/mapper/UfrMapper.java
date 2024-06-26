package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.service.dto.UfrDTO;
import sn.ugb.gir.service.dto.UniversiteDTO;

/**
 * Mapper for the entity {@link Ufr} and its DTO {@link UfrDTO}.
 */
@Mapper(componentModel = "spring")
public interface UfrMapper extends EntityMapper<UfrDTO, Ufr> {
    @Mapping(target = "universite", source = "universite", qualifiedByName = "universiteId")
    UfrDTO toDto(Ufr s);

    @Named("universiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UniversiteDTO toDtoUniversiteId(Universite universite);
}
