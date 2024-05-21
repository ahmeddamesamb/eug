package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.UFR;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.service.dto.UFRDTO;
import sn.ugb.gir.service.dto.UniversiteDTO;

/**
 * Mapper for the entity {@link UFR} and its DTO {@link UFRDTO}.
 */
@Mapper(componentModel = "spring")
public interface UFRMapper extends EntityMapper<UFRDTO, UFR> {
    @Mapping(target = "universite", source = "universite", qualifiedByName = "universiteNomUniversite")
    UFRDTO toDto(UFR s);

    @Named("universiteNomUniversite")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomUniversite", source = "nomUniversite")
    UniversiteDTO toDtoUniversiteNomUniversite(Universite universite);
}
