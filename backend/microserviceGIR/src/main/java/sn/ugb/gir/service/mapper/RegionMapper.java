package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Pays;
import sn.ugb.gir.domain.Region;
import sn.ugb.gir.service.dto.PaysDTO;
import sn.ugb.gir.service.dto.RegionDTO;

/**
 * Mapper for the entity {@link Region} and its DTO {@link RegionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {
    @Mapping(target = "pays", source = "pays", qualifiedByName = "paysId")
    RegionDTO toDto(Region s);

    @Named("paysId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaysDTO toDtoPaysId(Pays pays);
}
